import { Component, Input } from "@angular/core";

@Component({
  selector: "app-empty-state",
  standalone: true,
  template: `
    <div class="empty">
      <svg
        class="empty-icon"
        width="40"
        height="40"
        viewBox="0 0 40 40"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
        aria-hidden="true"
      >
        <circle
          cx="17"
          cy="17"
          r="11"
          stroke="currentColor"
          stroke-width="2.2"
        />
        <line
          x1="25.2"
          y1="25.2"
          x2="34"
          y2="34"
          stroke="currentColor"
          stroke-width="2.2"
          stroke-linecap="round"
        />
      </svg>
      <h3>{{ title }}</h3>
      <p>{{ message }}</p>
    </div>
  `,
})
export class EmptyStateComponent {
  @Input() title = "No records found";
  @Input() message = "Try a different search term or clear the filters.";
}
