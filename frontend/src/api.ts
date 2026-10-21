export type Status = 'OPEN' | 'IN_PROGRESS' | 'RESOLVED' | 'CANCELLED';
export type Priority = 'LOW' | 'MEDIUM' | 'HIGH';

export interface Summary {
  id: number;
  externalReference: string;
  category: string;
  priority: Priority;
  status: Status;
  createdAt: string;
}

export interface Detail extends Summary {
  customerId: string;
  description: string;
  channel: string;
  updatedAt: string;
}

export interface NewOrder {
  externalReference: string;
  customerId: string;
  category: string;
  description: string;
  channel: string;
  priority: Priority;
}

// Debe reflejar las reglas del backend (OrderStatus.next()).
export const TRANSITIONS: Record<Status, Status[]> = {
  OPEN: ['IN_PROGRESS', 'CANCELLED'],
  IN_PROGRESS: ['RESOLVED', 'CANCELLED'],
  RESOLVED: [],
  CANCELLED: [],
};

async function request<T>(url: string, init?: RequestInit): Promise<T> {
  const res = await fetch(url, {
    ...init,
    headers: { 'Content-Type': 'application/json' },
  });
  const data = await res.json().catch(() => null);
  if (!res.ok) throw new Error(data?.message ?? `Error ${res.status}`);
  return data as T;
}

const BASE = '/api/support-orders';

export const api = {
  list: (status?: Status) => request<Summary[]>(status ? `${BASE}?status=${status}` : BASE),
  get: (id: number) => request<Detail>(`${BASE}/${id}`),
  create: (o: NewOrder) => request<Detail>(BASE, { method: 'POST', body: JSON.stringify(o) }),
  changeStatus: (id: number, status: Status) =>
    request<Detail>(`${BASE}/${id}/status`, { method: 'PATCH', body: JSON.stringify({ status }) }),
};
