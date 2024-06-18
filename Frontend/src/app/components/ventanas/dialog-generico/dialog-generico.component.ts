import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'dialog-generico',
  templateUrl: './dialog-generico.component.html',
  styleUrls: ['./dialog-generico.component.css']
})
export class DialogGenericoComponent {
  soloAceptar:boolean = false;
  sinBotones:boolean = false;
  captionBoton:string = 'Aceptar';
  constructor(
    public dialogRef: MatDialogRef<DialogGenericoComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.sinBotones = data.sinBotones;
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }

  onAccept(): void {
    this.dialogRef.close(true);
  }
}
