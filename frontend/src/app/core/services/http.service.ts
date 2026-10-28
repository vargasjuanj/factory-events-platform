import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { ErrorModel } from '../models/error.model';

@Injectable({
  providedIn: 'root'
})
export class HttpService {

  private http = inject(HttpClient);

  get<T>(endpoint: string, params?: HttpParams): Observable<T> {
    return this.http.get<T>(endpoint, { params }).pipe(
      catchError(err => this.handleError(err))
    );
  }

  post<T>(endpoint: string, body: unknown): Observable<T> {
    return this.http.post<T>(endpoint, body).pipe(
      catchError(err => this.handleError(err))
    );
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    const errorModel: ErrorModel = {
      status: error.status,
      title: error.error?.title || 'Servidor no disponible',
      detail: error.error?.detail || 'No se pudo conectar con el servidor backend (puerto 8080).'
    };

    return throwError(() => errorModel);
  }
}
