import { Component, Output } from '@angular/core';
import {DashboardService} from "../../dashboard.service";

@Component({
  selector: 'sidebar-header',
  templateUrl: './sidebar-header.component.html',
})
export class SidebarHeaderComponent {
  @Output() mobileOrientation: 'start' | 'end';
  styles: {
    mobileOrientation: {
      start: string;
      end: string;
    };
  };

  constructor(private dashboard: DashboardService) {
    this.mobileOrientation = 'end';
    this.styles = {
      mobileOrientation: {
        start: 'left-0',
        end: 'right-0',
      },
    };
  }

  sidebarOpen() {
    return this.dashboard.sidebarOpen;
  }
}
