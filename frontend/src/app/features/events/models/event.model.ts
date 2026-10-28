export type EventType = 'PRODUCTION' | 'QUALITY';

export interface EventRequest {
  eventId: string;
  eventType: EventType;
  description: string;
  timestamp: string;
}

export interface EventResponse extends EventRequest {
  id: number;
  createdAt: string;
}

export interface QualityAuditResponse {
  id: number;
  eventId: string;
  description: string;
  auditTimestamp: string;
}
