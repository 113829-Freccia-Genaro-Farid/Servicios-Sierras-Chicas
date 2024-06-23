import {Component, Inject, OnDestroy, OnInit} from '@angular/core';
import {TurnoDTO} from "../../../../DTOs/turno-dto";
import {Subscription} from 'rxjs';
import {TurnosService} from "../../../../services/turnosService/turnos.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {UsuarioService} from "../../../../services/usuariosService/usuario.service";
import {MensajeRespuesta} from "../../../../models/mensaje-respuesta";
import {ProfesionistasService} from "../../../../services/profesionistasService/profesionistas.service";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {Profesionista} from "../../../../models/profesionista";
import {Turno} from "../../../../models/turno";
import {DialogGenericoComponent} from "../../../ventanas/dialog-generico/dialog-generico.component";

@Component({
  selector: 'agregar-turno',
  templateUrl: './agregar-turno.component.html',
  styleUrls: ['./agregar-turno.component.css']
})
export class AgregarTurnoComponent implements OnInit, OnDestroy{
  private subscription:Subscription | undefined;
  mensaje:MensajeRespuesta = {} as MensajeRespuesta;
  formTurno: FormGroup = this.fb.group({});
  profesionista:Profesionista = {} as Profesionista;
  infoTurno:Turno = {} as Turno;
  mov:number = 0;
  titulo : string = 'Agregar Turno';
  boton: string = 'Añadir';

  constructor(private turnosService:TurnosService,
              private fb: FormBuilder,
              private alerta: MatSnackBar,
              private profesionistasService: ProfesionistasService,
              private dialog: MatDialog,
              private userServices:UsuarioService,
              private dialogRef: MatDialogRef<AgregarTurnoComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    this.infoTurno = data.turno as Turno;
    this.mov = data.mov;
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }

  ngOnInit(): void {
    this.subscription = new Subscription();

    this.subscription?.add(
      this.profesionistasService.getProfesionistaByUserEmail(this.userServices.getUsuarioLogueado().email).subscribe(
        {
          next:(response)=>{
            this.profesionista = response;
          }
        }
      )
    );

    this.formTurno = this.fb.group({
      title: ['', [Validators.required]],
      date: ['', [Validators.required]]
    });

    if (this.mov == 1){
      this.titulo = 'Modificar Turno';
      this.boton = 'Modificar';
      this.title?.setValue(this.infoTurno.descripcion);
      this.date?.setValue(this.infoTurno.fechaTurno);
    }

  }

  agregarTurno(){
    let turnoDTO :TurnoDTO = {
      descripcion: this.title?.value,
      fechaTurno: this.date?.value,
      idProfesionista: this.profesionista.id,
      idCliente: null,
    }
    if(this.mov == 0){
      this.subscription?.add(
        this.turnosService.postTurno(turnoDTO).subscribe({
          next:(response)=>{
            this.mensaje = response;
            if (response.ok){
              this.openSnackBar(this.mensaje.mensaje);
              if (this.mensaje.ok) {
                setTimeout(() => {
                  this.dialogRef.close();
                }, 3000);
              }
            }else{
              this.openSnackBar(this.mensaje.mensaje);
            }
          },
          error:(response)=>{
            this.mensaje = response;
            this.openSnackBar(this.mensaje.mensaje);
          }
        })
      )
    }else{
      this.subscription?.add(
        this.turnosService.putTurno(turnoDTO, this.infoTurno.idTurno).subscribe({
          next:(response)=>{
            this.mensaje = response;
            if (response.ok){
              this.openSnackBar(this.mensaje.mensaje);
              if (this.mensaje.ok) {
                setTimeout(() => {
                  this.dialogRef.close();
                }, 1000);
              }
            }else{
              this.openSnackBar(this.mensaje.mensaje);
            }
          },
          error:(response)=>{
            this.mensaje = response;
            this.openSnackBar(this.mensaje.mensaje);
          }
        })
      )
    }
  }

  get date(){
    return this.formTurno.get('date');
  }
  get title(){
    return this.formTurno.get('title');
  }

  openSnackBar(mensaje:string) {
    this.alerta.open(mensaje,"Cerrar",{duration:3000});
  }

  eliminarTurno() {
    const dialogRef = this.dialog.open(DialogGenericoComponent, {
      data: {
        title: 'Borrar turno',
        sinBotones: false,
        descripcion: '¿Estás seguro/a de eliminar el turno?'
      }
    });

    dialogRef.afterClosed().subscribe(result1 => {
      if (result1) {
        this.turnosService.deleteTurno(this.infoTurno.idTurno).subscribe({
          next:(response)=>{
            this.mensaje = response;
            if (response.ok){
              this.openSnackBar(this.mensaje.mensaje);
              if (this.mensaje.ok) {
                setTimeout(() => {
                  this.dialogRef.close();
                }, 3000);
              }
            }else{
              this.openSnackBar(this.mensaje.mensaje);
            }
          },
          error:(response)=>{
            this.mensaje = response;
            this.openSnackBar(this.mensaje.mensaje);
          }
        })
      }
    });
  }
}
