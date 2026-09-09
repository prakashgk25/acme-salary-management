import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-loading',
  standalone: true,
  template: `
    <div class="skeleton-table" [attr.aria-label]="label" role="status">
      @for (row of rowsArray(); track row) {
        <div class="skeleton-row">
          <span class="skeleton-chip"></span>
          <span class="skeleton-bar w-60"></span>
          <span class="skeleton-bar w-40"></span>
          <span class="skeleton-bar w-30"></span>
          <span class="skeleton-bar w-45"></span>
          <span class="skeleton-bar w-30"></span>
        </div>
      }
    </div>
  `
})
export class LoadingComponent {
  @Input() rows = 6;
  @Input() label = 'Loading records';

  rowsArray(): number[] {
    return Array.from({ length: this.rows }, (_, i) => i);
  }
}
