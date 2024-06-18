import { Component } from '@angular/core';

@Component({
  selector: 'seleccion-rol',
  templateUrl: './seleccion-rol.component.html',
  styleUrls: ['./seleccion-rol.component.css']
})
export class SeleccionRolComponent {
  hover(side: string) {
    const left = document.querySelector('.left');
    const right = document.querySelector('.right');

    if (side === 'left' && left) {
      left.classList.toggle('active');
      if (right) {
        right.classList.remove('active');
      }
    } else if (side === 'right' && right) {
      right.classList.toggle('active');
      if (left) {
        left.classList.remove('active');
      }
    }
  }
}
