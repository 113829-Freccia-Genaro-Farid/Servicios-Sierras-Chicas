import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Observable, Subscription} from "rxjs";
import {MensajeRespuesta} from "../../../models/mensaje-respuesta";
import {AuxiliaresService} from "../../../services/auxiliaresService/auxiliares.service";
import {Ciudad} from "../../../models/Auxiliares/ciudad";
import {TipoDNI} from "../../../models/Auxiliares/tipo-dni";
import {Provincia} from "../../../models/Auxiliares/provincia";
import {Persona} from "../../../models/persona";
import {PersonasService} from "../../../services/personasService/personas.service";
import {Usuario} from "../../../models/usuario";
import {UsuarioService} from "../../../services/usuariosService/usuario.service";
import {DatePipe} from "@angular/common";
import {PersonaDTOPut} from "../../../DTOs/persona-dtoput";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-modificar-persona',
  templateUrl: './modificar-persona.component.html',
  styleUrls: ['./modificar-persona.component.css']
})
export class ModificarPersonaComponent implements OnInit, OnDestroy{
  private subscription:Subscription | undefined;
  private usuarioLogeado: Usuario = {} as Usuario;
  mensajeRespuesta:MensajeRespuesta = {} as MensajeRespuesta;
  personaForm: FormGroup = this.fb.group({});
  datosPersona : Persona = {} as Persona;
  ciudades: Ciudad[] = [];
  provincias: Provincia[] = [];
  tiposDNI: TipoDNI[] = [];

  constructor(
    private fb: FormBuilder,
    private auxiliaresService: AuxiliaresService,
    private personasService:PersonasService,
    private usuariosService:UsuarioService,
    private alerta: MatSnackBar
  ) {}

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }

  ngOnInit(): void {
    this.subscription = new Subscription();

    this.usuarioLogeado = this.usuariosService.getUsuarioLogueado();

    this.subscription.add(
      this.auxiliaresService.getProvincias().subscribe({
        next:(response)=>{
          this.provincias = response;
        },
        error:()=>{
          alert('Error al cargar las provincias');
        }
      })
    )

    this.subscription.add(
      this.auxiliaresService.getTiposDNI().subscribe({
        next:(response)=>{
          this.tiposDNI = response;
        },
        error:()=>{
          alert('Error al cargar los tipos de documento');
        }
      })
    )

    this.personaForm = this.fb.group({
      apellido: [null, [Validators.required, Validators.maxLength(25)]],
      nombre: [null, [Validators.required, Validators.maxLength(25)]],
      provincia: [null, Validators.required],
      ciudad: [null, Validators.required],
      fechaNacimiento: [null, Validators.required],
      tipoDNI: [null, Validators.required],
      nroDocumento: [null, [Validators.required, Validators.maxLength(10)]],
      calle: [null, [Validators.required, Validators.maxLength(50)]],
      altura: [null, [Validators.required, Validators.maxLength(10)]],
      telefono1: [null, [Validators.required, Validators.maxLength(12)]],
      telefono2: [null, Validators.maxLength(12)],
      telefonofijo: [null, Validators.maxLength(12)]
    });
    this.personaForm.controls['provincia'].valueChanges.subscribe((value) => {
      this.cargarCiudades(value);
    })
    this.provincia?.setValue(5);
    this.provincia?.disable();

    this.subscription.add(
      this.personasService.getDatosPersonaByUser(this.usuarioLogeado.email).subscribe({
        next: (response) => {
          this.datosPersona = response;
          this.personaForm.patchValue(this.datosPersona);
          this.ciudad?.setValue(this.ciudades.find(tipo => tipo.descripcion === this.datosPersona.ciudad)?.id);
          this.tipoDNI?.setValue(this.tiposDNI.find(tipo => tipo.descripcion === this.datosPersona.tipoDNI)?.id);
        },
        error: () => {
          alert('Error al cargar los datos de la persona');
        }
      })
    )
  }


  actualizarDatos(): void {
    let persona: PersonaDTOPut= {
      altura: this.altura?.value,
      apellido: this.apellido?.value,
      calle: this.calle?.value,
      idCiudad: this.ciudad?.value,
      fechaNacimiento: this.fechaNacimiento?.value,
      nombre: this.nombre?.value,
      nroDocumento: this.nroDocumento?.value,
      telefono1: this.telefono1?.value,
      telefono2: this.telefono2?.value,
      telefonofijo: this.telefonofijo?.value,
      idTipoDNI: this.tipoDNI?.value
    }

    this.subscription?.add(
      this.personasService.putPersona(persona, this.datosPersona.id).subscribe({
        next:(response)=>{
          this.mensajeRespuesta = response;
          this.openSnackBar(this.mensajeRespuesta.mensaje);
          if (this.mensajeRespuesta.ok){
            this.personaForm.reset();
          }
        },
        error:(response)=>{
          this.mensajeRespuesta = response;
          this.openSnackBar(this.mensajeRespuesta.mensaje);
        }
      })
    )
  }
  openSnackBar(mensaje:string) {
    this.alerta.open(mensaje,"Cerrar",{duration:3000});
  }

  cargarCiudades(idProvincia:number){
    this.subscription?.add(
      this.auxiliaresService.getCiudadesByProvincia(idProvincia).subscribe(
        (response:Ciudad[])=>{
          this.ciudades=response;
        }
      )
    );
  }
  get apellido() {
    return this.personaForm.get('apellido');
  }

  get nombre() {
    return this.personaForm.get('nombre');
  }
  get provincia() {
    return this.personaForm.get('provincia');
  }

  get ciudad() {
    return this.personaForm.get('ciudad');
  }

  get fechaNacimiento() {
    return this.personaForm.get('fechaNacimiento');
  }

  get tipoDNI() {
    return this.personaForm.get('tipoDNI');
  }

  get nroDocumento() {
    return this.personaForm.get('nroDocumento');
  }

  get calle() {
    return this.personaForm.get('calle');
  }

  get altura() {
    return this.personaForm.get('altura');
  }

  get telefono1() {
    return this.personaForm.get('telefono1');
  }

  get telefono2() {
    return this.personaForm.get('telefono2');
  }

  get telefonofijo() {
    return this.personaForm.get('telefonofijo');
  }


}
