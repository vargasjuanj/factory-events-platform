import { Injectable, inject } from '@angular/core';
import { BehaviorSubject, Observable, finalize, forkJoin, tap } from 'rxjs';
import { HttpService } from '../../../core/services/http.service';
import { EndPoints } from '../../../core/end-points';
import { EventRequest, EventResponse, QualityAuditResponse } from '../models/event.model';

@Injectable({
  providedIn: 'root'
})
export class EventService {

  private httpService = inject(HttpService);

  private eventsSubject = new BehaviorSubject<EventResponse[]>([]);
  readonly events$ = this.eventsSubject.asObservable();

  private auditsSubject = new BehaviorSubject<QualityAuditResponse[]>([]);
  readonly audits$ = this.auditsSubject.asObservable();

  private loadingSubject = new BehaviorSubject<boolean>(false);
  readonly loading$ = this.loadingSubject.asObservable();

  loadInitialData(): Observable<[EventResponse[], QualityAuditResponse[]]> {
    this.loadingSubject.next(true);

    return forkJoin([
      this.httpService.get<EventResponse[]>(EndPoints.EVENTS),
      this.httpService.get<QualityAuditResponse[]>(EndPoints.AUDITS)
    ]).pipe(
      tap(([events, audits]) => {
        this.eventsSubject.next(events);
        this.auditsSubject.next(audits);
      }),
      finalize(() => this.loadingSubject.next(false))
    );
  }

  loadAudits(): Observable<QualityAuditResponse[]> {
    return this.httpService.get<QualityAuditResponse[]>(EndPoints.AUDITS).pipe(
      tap(audits => this.auditsSubject.next(audits))
    );
  }

  registerEvent(payload: EventRequest): Observable<EventResponse> {
    return this.httpService.post<EventResponse>(EndPoints.EVENTS, payload).pipe(
      tap(created => {
        const current = this.eventsSubject.getValue();
        this.eventsSubject.next([created, ...current]);

        if (created.eventType === 'QUALITY') {
          this.loadAudits().subscribe();
        }
      })
    );
  }
}
