import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatSnackBar } from '@angular/material/snack-bar';
import { finalize } from 'rxjs';
import { EventService } from '../../services/event.service';
import { EventRequest } from '../../models/event.model';
import { TableColumn } from '../../../../shared/models/table-column.model';
import { DynamicCrudTableComponent } from '../../../../shared/components/dynamic-crud-table/dynamic-crud-table.component';
import { EventFormComponent } from '../../components/event-form/event-form.component';
import { MaterialModule } from '../../../../shared/material.module';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MaterialModule,
    DynamicCrudTableComponent,
    EventFormComponent
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {

  public eventService = inject(EventService);
  private snackBar = inject(MatSnackBar);

  isSubmitting = false;

  readonly eventColumns: TableColumn[] = [
    { key: 'eventId', header: 'ID Evento' },
    { key: 'eventType', header: 'Tipo', type: 'badge' },
    { key: 'description', header: 'Descripción' },
    { key: 'timestamp', header: 'Fecha y Hora', type: 'date' }
  ];

  readonly auditColumns: TableColumn[] = [
    { key: 'eventId', header: 'ID Evento' },
    { key: 'description', header: 'Descripción' },
    { key: 'auditTimestamp', header: 'Fecha Auditoría', type: 'date' }
  ];

  ngOnInit(): void {
    this.eventService.loadInitialData().subscribe({
      error: (err) => this.snackBar.open(err.detail, 'Cerrar', {
        duration: 5000,
        verticalPosition: 'top',
        horizontalPosition: 'center',
        panelClass: ['snackbar-error']
      })
    });
  }

  onEventSubmit(payload: EventRequest): void {
    this.isSubmitting = true;

    this.eventService.registerEvent(payload)
      .pipe(finalize(() => this.isSubmitting = false))
      .subscribe({
        next: (created) => {
          this.snackBar.open(
            `Evento '${created.eventId}' (${created.eventType}) registrado con éxito.`,
            'Cerrar',
            {
              duration: 4000,
              verticalPosition: 'top',
              horizontalPosition: 'center',
              panelClass: ['snackbar-success']
            }
          );
        },
        error: (err) => {
          this.snackBar.open(err.detail, 'Cerrar', {
            duration: 5000,
            verticalPosition: 'top',
            horizontalPosition: 'center',
            panelClass: ['snackbar-error']
          });
        }
      });
  }
}
