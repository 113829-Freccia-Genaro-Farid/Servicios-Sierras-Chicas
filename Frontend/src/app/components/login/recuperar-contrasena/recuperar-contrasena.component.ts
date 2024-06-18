import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MensajeRespuesta} from "../../../models/mensaje-respuesta";
import {UsuarioService} from "../../../services/usuariosService/usuario.service";
import {Subscription} from "rxjs";
import {DialogGenericoComponent} from "../../ventanas/dialog-generico/dialog-generico.component";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {CambiarContraseniaComponent} from "../cambiar-contrasenia/cambiar-contrasenia.component";

@Component({
  selector: 'recuperar-contrasena',
  templateUrl: './recuperar-contrasena.component.html',
  styleUrls: ['./recuperar-contrasena.component.css']
})
export class RecuperarContrasenaComponent implements OnInit, OnDestroy{
  form:FormGroup = this.fb.group({});
  mensaje:MensajeRespuesta = {} as MensajeRespuesta;
  subscription:Subscription = new Subscription();
  correoEnviado: boolean = false;


  constructor(private fb: FormBuilder,
              private alerta: MatSnackBar,
              private usuarioService:UsuarioService,
              private dialog: MatDialog,
              private dialogRef: MatDialogRef<RecuperarContrasenaComponent>) {}

  ngOnInit() {
    this.form = this.fb.group({
      email: [null, [Validators.required, Validators.email]]
    });
  }

  async enviarCorreo() {
    this.correoEnviado = true;
    this.subscription.add(
      this.usuarioService.enviarCorreo(this.email?.value).subscribe({
        next: async (response: MensajeRespuesta) => {
          this.mensaje = response as MensajeRespuesta;
          this.openSnackBar(this.mensaje.mensaje);
          if (this.mensaje.ok){
            setTimeout(() => {
              this.dialogRef.close();
              this.openDialog()
            }, 3000);
          }else{
            this.correoEnviado = false;
          }
        },
        error: async () => {
          this.mensaje = { mensaje: "Error al enviar correo", ok: false };
          this.correoEnviado = false;
          this.openSnackBar(this.mensaje.mensaje);
        }
      })
    );
  }

  openSnackBar(mensaje:string) {
    this.alerta.open(mensaje,"Cerrar",{duration:3000});
  }

  openDialog(): void {
    this.dialog.open(DialogGenericoComponent, {
      data: {
        title: 'Cambiar contraseña',
        component: CambiarContraseniaComponent,
        email: this.email?.value,
        solicitaCodigo:true,
        sinBotones:true
      }
    });
  }


  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  get email(){
    return this.form.get('email');
  }
}
