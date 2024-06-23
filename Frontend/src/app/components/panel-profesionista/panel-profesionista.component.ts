import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { Profesion } from '../../models/Auxiliares/profesion';
import { AuxiliaresService } from '../../services/auxiliaresService/auxiliares.service';
import { Profesionista } from '../../models/profesionista';
import { ProfesionistasService } from '../../services/profesionistasService/profesionistas.service';
import { UsuarioService } from '../../services/usuariosService/usuario.service';
import { MensajeRespuesta } from '../../models/mensaje-respuesta';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ProfesionistaDTOPut } from '../../DTOs/profesionista-dtoput';

@Component({
  selector: 'panel-profesionista',
  templateUrl: './panel-profesionista.component.html',
  styleUrls: ['./panel-profesionista.component.css']
})
export class PanelProfesionistaComponent implements OnInit, OnDestroy {
  private subscription: Subscription = new Subscription();
  mensaje: MensajeRespuesta = {} as MensajeRespuesta;
  profesionista: Profesionista = {} as Profesionista;
  profesionistaForm: FormGroup = this.fb.group({});
  descripcionForm: FormGroup = this.fb.group({});
  profesiones: Profesion[] = [];
  editModeDatos: boolean = false;
  editModeDesc: boolean = false;

  constructor(private fb: FormBuilder,
              private auxiliarService: AuxiliaresService,
              private profesionistaService: ProfesionistasService,
              private usuarioService: UsuarioService,
              private alerta: MatSnackBar) {}

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  ngOnInit(): void {
    this.profesionistaForm = this.fb.group({
      poseeMatricula: { value: false, disabled: true },
      nroMatricula: [{ value: '', disabled: true }, [Validators.required]],
      comunicacionWsp: { value: false, disabled: true },
      profesiones: { value: [], disabled: true },
    });
    this.descripcionForm = this.fb.group({
      descripcion: { value: false, disabled: true },
    });

    this.subscription.add(
      this.auxiliarService.getProfesiones().subscribe({
        next: (response) => {
          this.profesiones = response;
          this.cargarDatos();
        }
      })
    );

    this.profesionistaForm.get('poseeMatricula')?.valueChanges.subscribe(value => {
      if (!value) {
        this.profesionistaForm.get('nroMatricula')?.setValue('');
      }
    });
  }

  toggleMatricula(checked: boolean) {
    const nroMatriculaControl = this.profesionistaForm.get('nroMatricula');
    checked ? nroMatriculaControl?.enable() : nroMatriculaControl?.disable();
  }

  cancelEdit() {
    this.editModeDatos = false;
    this.cargarDatos();
    this.disableControls(this.profesionistaForm);
  }

  cancelEditDesc() {
    this.editModeDesc = false;
    this.cargarDatos();
    this.disableControls(this.descripcionForm);
  }

  saveChanges() {
    if (this.profesionistaForm.dirty) {
      const dtoPut: ProfesionistaDTOPut = {
        comunicacionWsp: this.profesionistaForm.get('comunicacionWsp')?.value,
        nroMatricula: this.profesionistaForm.get('nroMatricula')?.value,
        poseeMatricula: this.profesionistaForm.get('poseeMatricula')?.value,
        idProfesiones: this.profesionistaForm.get('profesiones')?.value
      };

      this.subscription.add(
        this.profesionistaService.putProfesionista(this.profesionista.id, dtoPut).subscribe({
          next: (response) => {
            this.mensaje = response;
            this.openSnackBar(this.mensaje.mensaje);
            this.cargarDatos();
          },
          error: (response) => {
            this.mensaje = response;
            this.openSnackBar(this.mensaje.mensaje);
          }
        })
      );
    }

    this.editModeDatos = false;
    this.disableControls(this.profesionistaForm);
  }

  habilitar() {
    this.editModeDatos = true;
    this.enableControls(this.profesionistaForm);
    if (!this.profesionistaForm.get('poseeMatricula')?.value) {
      this.profesionistaForm.get('nroMatricula')?.disable();
    }
  }

  cargarDatos() {
    this.subscription.add(
      this.profesionistaService.getProfesionistaByUserEmail(this.usuarioService.getUsuarioLogueado().email).subscribe({
        next: (response) => {
          this.profesionista = response;
          const ids = response.profesiones
            .map(descripcion => this.profesiones.find(profesion => profesion.descripcion === descripcion)?.id)
            .filter(id => id !== undefined);
          this.profesionistaForm.get('profesiones')?.setValue(ids);
          this.profesionistaForm.get('comunicacionWsp')?.setValue(response.comunicacionWsp);
          this.profesionistaForm.get('poseeMatricula')?.setValue(response.poseeMatricula);
          if (response.poseeMatricula) {
            this.profesionistaForm.get('nroMatricula')?.setValue(response.nroMatricula);
          }
          this.descripcionForm.get('descripcion')?.setValue(response.presentacion);
        }
      })
    );
  }

  saveChangesDesc() {
    if (this.descripcionForm.get('descripcion')?.value !== this.profesionista.presentacion) {
      this.subscription.add(
        this.profesionistaService.putPresentacionProfesionista(this.profesionista.id, this.descripcionForm.get('descripcion')?.value).subscribe({
          next: (response) => {
            this.mensaje = response;
            this.openSnackBar(this.mensaje.mensaje);
            this.cargarDatos();
          },
          error: (response) => {
            this.mensaje = response;
            this.openSnackBar(this.mensaje.mensaje);
          }
        })
      );
    }

    this.editModeDesc = false;
    this.disableControls(this.descripcionForm);
  }

  openSnackBar(mensaje: string) {
    this.alerta.open(mensaje, 'Cerrar', { duration: 3000 });
  }

  private disableControls(form: FormGroup) {
    Object.keys(form.controls).forEach(control => {
      form.controls[control].disable();
    });
  }

  private enableControls(form: FormGroup) {
    Object.keys(form.controls).forEach(control => {
      form.controls[control].enable();
    });
  }
}
