import { FormEvent, useCallback, useEffect, useState } from 'react';
import { api, Detail, NewOrder, Priority, Status, Summary, TRANSITIONS } from './api';

const STATUSES: Status[] = ['OPEN', 'IN_PROGRESS', 'RESOLVED', 'CANCELLED'];
const EMPTY: NewOrder = {
  externalReference: '', customerId: '', category: '', description: '', channel: 'MOBILE_APP', priority: 'MEDIUM',
};

const fmt = (iso: string) => new Date(iso).toLocaleString();

function validate(o: NewOrder): Partial<Record<keyof NewOrder, string>> {
  const e: Partial<Record<keyof NewOrder, string>> = {};
  if (!o.externalReference.trim()) e.externalReference = 'Requerido';
  if (!o.customerId.trim()) e.customerId = 'Requerido';
  if (!o.category.trim()) e.category = 'Requerido';
  if (!o.channel.trim()) e.channel = 'Requerido';
  if (o.description.trim().length < 10 || o.description.length > 500)
    e.description = 'Entre 10 y 500 caracteres';
  return e;
}

export default function App() {
  const [orders, setOrders] = useState<Summary[]>([]);
  const [filter, setFilter] = useState<Status | ''>('');
  const [selected, setSelected] = useState<Detail | null>(null);
  const [form, setForm] = useState<NewOrder>(EMPTY);
  const [touched, setTouched] = useState(false);
  const [loading, setLoading] = useState(false);
  const [msg, setMsg] = useState<{ type: 'ok' | 'error'; text: string } | null>(null);

  const errors = validate(form);

  // Ejecuta una acción mostrando estado de carga y resultado (éxito/error).
  const run = useCallback(async (action: () => Promise<string | void>) => {
    setLoading(true);
    try {
      const ok = await action();
      setMsg(ok ? { type: 'ok', text: ok } : null);
    } catch (e) {
      setMsg({ type: 'error', text: (e as Error).message });
    } finally {
      setLoading(false);
    }
  }, []);

  const load = useCallback(() => run(async () => setOrders(await api.list(filter || undefined))), [filter, run]);
  useEffect(() => { load(); }, [load]);

  const open = (id: number) => run(async () => setSelected(await api.get(id)));

  const submit = (ev: FormEvent) => {
    ev.preventDefault();
    setTouched(true);
    if (Object.keys(errors).length) return;
    run(async () => {
      const created = await api.create(form);
      setForm(EMPTY);
      setTouched(false);
      setSelected(created);
      setOrders(await api.list(filter || undefined));
      return `Orden ${created.externalReference} creada`;
    });
  };

  const change = (status: Status) =>
    selected &&
    run(async () => {
      setSelected(await api.changeStatus(selected.id, status));
      setOrders(await api.list(filter || undefined));
      return `Estado actualizado a ${status}`;
    });

  const field = (k: keyof NewOrder, label: string) => (
    <label>
      {label}
      <input value={form[k]} onChange={(e) => setForm({ ...form, [k]: e.target.value })} />
      {touched && errors[k] && <small className="err">{errors[k]}</small>}
    </label>
  );

  return (
    <main>
      <h1>Órdenes de soporte</h1>
      {loading && <p className="info">Cargando…</p>}
      {msg && <p className={msg.type === 'ok' ? 'ok' : 'error'}>{msg.text}</p>}

      <div className="grid">
        <section>
          <h2>Listado</h2>
          <label>
            Estado
            <select value={filter} onChange={(e) => setFilter(e.target.value as Status | '')}>
              <option value="">Todos</option>
              {STATUSES.map((s) => <option key={s}>{s}</option>)}
            </select>
          </label>
          <div className="table-wrap">
            <table>
              <thead><tr><th>Ref.</th><th>Categoría</th><th>Prioridad</th><th>Estado</th><th>Creada</th></tr></thead>
              <tbody>
                {orders.map((o) => (
                  <tr key={o.id} onClick={() => open(o.id)} className={selected?.id === o.id ? 'sel' : ''}>
                    <td>{o.externalReference}</td><td>{o.category}</td>
                    <td><span className={`tag ${o.priority}`}>{o.priority}</span></td>
                    <td>{o.status}</td><td>{fmt(o.createdAt)}</td>
                  </tr>
                ))}
                {!orders.length && !loading && <tr><td colSpan={5}>Sin órdenes</td></tr>}
              </tbody>
            </table>
          </div>
        </section>

        <section>
          <h2>Detalle</h2>
          {selected ? (
            <div className="card">
              <p><b>{selected.externalReference}</b> · #{selected.id}</p>
              <p>{selected.description}</p>
              <p>Cliente: {selected.customerId} · Canal: {selected.channel} · Categoría: {selected.category}</p>
              <p>Prioridad: {selected.priority} · Estado: <b>{selected.status}</b></p>
              <p>Creada: {fmt(selected.createdAt)} · Actualizada: {fmt(selected.updatedAt)}</p>
              <div className="actions">
                {TRANSITIONS[selected.status].map((s) => (
                  <button key={s} disabled={loading} onClick={() => change(s)}>Pasar a {s}</button>
                ))}
                {!TRANSITIONS[selected.status].length && <em>Estado final, sin acciones</em>}
              </div>
            </div>
          ) : <p>Selecciona una orden del listado.</p>}

          <h2>Nueva orden</h2>
          <form onSubmit={submit} noValidate>
            {field('externalReference', 'Referencia externa')}
            {field('customerId', 'Cliente')}
            {field('category', 'Categoría')}
            {field('channel', 'Canal')}
            <label>
              Prioridad
              <select value={form.priority} onChange={(e) => setForm({ ...form, priority: e.target.value as Priority })}>
                <option>LOW</option><option>MEDIUM</option><option>HIGH</option>
              </select>
            </label>
            <label>
              Descripción ({form.description.length}/500)
              <textarea rows={3} value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} />
              {touched && errors.description && <small className="err">{errors.description}</small>}
            </label>
            <button type="submit" disabled={loading}>Crear orden</button>
          </form>
        </section>
      </div>
    </main>
  );
}
