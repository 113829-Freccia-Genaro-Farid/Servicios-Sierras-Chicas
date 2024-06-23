import {Component, Inject, ViewChild} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {AgregarTurnoComponent} from "../../turnos/agregar-turno/agregar-turno/agregar-turno.component";
import {Turno} from "../../../models/turno";

@Component({
  selector: 'dialog-generico',
  templateUrl: './dialog-generico.component.html',
  styleUrls: ['./dialog-generico.component.css']
})
export class DialogGenericoComponent {
  soloAceptar:boolean = false;
  sinBotones:boolean = false;
  captionBoton:string = 'Aceptar';
  descripcion:string = '';
  constructor(
    public dialogRef: MatDialogRef<DialogGenericoComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  )
  {
    this.sinBotones = data.sinBotones;
    this.descripcion = data.descripcion;
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }

  onAccept(): void {
    this.dialogRef.close(true);
  }
}
