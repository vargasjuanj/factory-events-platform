import { Component, EventEmitter, Input, Output, inject } from '@angular/core';
import { formatDate } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { EventRequest } from '../../models/event.model';
import { MaterialModule } from '../../../../shared/material.module';

@Component({
  selector: 'app-event-form',
  standalone: true,
  imports: [ReactiveFormsModule, MaterialModule],
  templateUrl: './event-form.component.html',
  styleUrl: './event-form.component.css'
})
export class EventFormComponent {

  @Input() isSubmitting = false;
  @Output() submitEvent = new EventEmitter<EventRequest>();

  private fb = inject(FormBuilder);

  readonly eventForm = this.fb.group({
    eventId: ['', [Validators.required, Validators.pattern(/^EVT-\d{4}$/)]],
    eventType: ['PRODUCTION', [Validators.required]],
    description: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(500)]],
    timestamp: [this.getCurrentDateTime(), [Validators.required]]
  });

  setCurrentTimestamp(): void {
    this.eventForm.patchValue({ timestamp: this.getCurrentDateTime() });
  }

  onEventIdInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const clean = input.value.toUpperCase().replace(/\s+/g, '');
    input.value = clean;
    this.eventForm.get('eventId')?.setValue(clean, { emitEvent: false });
  }

  onDescriptionInput(event: Event): void {
    const input = event.target as HTMLTextAreaElement;
    if (input.value.startsWith(' ')) {
      const clean = input.value.trimStart();
      input.value = clean;
      this.eventForm.get('description')?.setValue(clean, { emitEvent: false });
    }
  }

  generateSampleId(): void {
    const randomSuffix = Math.floor(1000 + Math.random() * 9000);
    this.eventForm.patchValue({ eventId: `EVT-${randomSuffix}` });
  }

  resetForm(): void {
    this.eventForm.reset({
      eventId: '',
      eventType: 'PRODUCTION',
      description: '',
      timestamp: this.getCurrentDateTime()
    });
  }

  onSubmit(): void {
    if (this.eventForm.invalid) {
      this.eventForm.markAllAsTouched();
      return;
    }

    this.submitEvent.emit(this.eventForm.value as EventRequest);
  }

  private getCurrentDateTime(): string {
    return formatDate(new Date(), 'yyyy-MM-ddTHH:mm:ss', 'en-US');
  }
}
