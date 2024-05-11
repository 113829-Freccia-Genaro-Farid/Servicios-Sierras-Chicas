import { Component, Input, OnInit } from '@angular/core';
import {MensajeRespuesta} from "../../../models/mensaje-respuesta";

@Component({
  selector: 'fn-alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.css']
})
export class AlertComponent implements OnInit {
  @Input() mensajeRespuesta:MensajeRespuesta = {} as MensajeRespuesta;
  showAlert: boolean = true;
  tipoAlerta:string='';

  ngOnInit(): void {
    if (this.mensajeRespuesta.ok){
      this.tipoAlerta = 'alert alert-success';
    }else{
      this.tipoAlerta = 'alert alert-danger';
    }
    setTimeout(() => {
      this.showAlert = false;
    }, 1000);
  }
}
