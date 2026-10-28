import { environment } from '../../environments/environment';

export const EndPoints = {
  EVENTS: `${environment.apiUrl}/events`,
  AUDITS: `${environment.apiUrl}/events/audits`
};
