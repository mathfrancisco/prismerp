import { Component, OnInit } from '@angular/core';
import {CommonModule, NgForOf} from '@angular/common';
import {RouterLink} from '@angular/router';

interface QuickAction {
  id: number;
  title: string;
  icon: string;
  color: string;
  route: string;
}

@Component({
  selector: 'app-control-panel',
  standalone: true,
  imports: [
    RouterLink,
    NgForOf,
    CommonModule
  ],
  templateUrl: './control-panel.component.html',
  styleUrl: './control-panel.component.scss'
})
export class ControlPanelComponent implements OnInit {
  quickActions: QuickAction[] = [
    {
      id: 1,
      title: 'New Sale',
      icon: 'bi bi-cart-plus',
      color: 'bg-blue-100 text-blue-600',
      route: '/sales/new'
    }]

  ngOnInit(): void {
  }
}
