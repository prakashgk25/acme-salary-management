import { Component, inject } from "@angular/core";
import { ToastService } from "../../services/toast.service";

@Component({
  selector: "app-toast-stack",
  standalone: true,
  template: `
    <div class="toast-stack" role="status" aria-live="polite">
      @for (toast of toastService.toasts(); track toast.id) {
        <div class="toast" [class]="toast.kind">
          <span class="toast-dot"></span>
          <p>{{ toast.message }}</p>
          <button
            type="button"
            class="toast-close"
            (click)="toastService.dismiss(toast.id)"
            aria-label="Dismiss"
          >
            ×
          </button>
        </div>
      }
    </div>
  `,
})
export class ToastStackComponent {
  toastService = inject(ToastService);
}
