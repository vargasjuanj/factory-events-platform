import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableColumn } from '../../models/table-column.model';
import { MaterialModule } from '../../material.module';

@Component({
  selector: 'app-dynamic-crud-table',
  standalone: true,
  imports: [CommonModule, MaterialModule],
  templateUrl: './dynamic-crud-table.component.html',
  styleUrl: './dynamic-crud-table.component.css'
})
export class DynamicCrudTableComponent {

  @Input() columns: TableColumn[] = [];
  @Input() data: any[] = [];
  @Input() emptyMessage = 'No hay registros disponibles.';

  @Input() canEdit = false;
  @Input() canDelete = false;
  @Output() edit = new EventEmitter<any>();
  @Output() delete = new EventEmitter<any>();

  get displayedColumns(): string[] {
    const cols = this.columns.map(c => c.key);
    if (this.canEdit || this.canDelete) {
      cols.push('actions');
    }
    return cols;
  }
}
