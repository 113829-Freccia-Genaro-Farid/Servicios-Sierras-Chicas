import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ProfesionistasService} from "../../services/profesionistasService/profesionistas.service";
import {Profesionista} from "../../models/profesionista";
import {Subscription} from "rxjs";
import {ActivatedRoute, Params} from "@angular/router";
import {Resenia} from "../../models/resenia";
import {MatIconRegistry} from "@angular/material/icon";
import {DomSanitizer} from "@angular/platform-browser";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UsuarioService} from "../../services/usuariosService/usuario.service";
import {Roles} from "../../models/Auxiliares/roles";
import {ReseniaDTO} from "../../DTOs/resenia-dto";
import {MensajeRespuesta} from "../../models/mensaje-respuesta";
import {Cliente} from "../../models/cliente";
import {ClientesService} from "../../services/clientesService/clientes.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {ReseniaStats} from "../../models/resenia-stats";
import {GraficoBarraResenasComponent} from "../../charts/grafico-barra-resenas/grafico-barra-resenas.component";
import {Persona} from "../../models/persona";

const SVG_STAR =
  `
  <svg xmlns="http://www.w3.org/2000/svg" enable-background="new 0 0 24 24" height="24px" viewBox="0 0 24 24" width="24px" fill="#FFFF55"><g><path d="M0 0h24v24H0V0z" fill="none"/><path d="M0 0h24v24H0V0z" fill="none"/></g><g><path d="M12 17.27 18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21 12 17.27z"/></g></svg>
`;
const SVG_STAR_VACIA =
  `
  <svg xmlns="http://www.w3.org/2000/svg" enable-background="new 0 0 24 24" height="24px" viewBox="0 0 24 24" width="24px" fill="#FFFFFF"><g><path d="M0 0h24v24H0V0z" fill="none"/><path d="M0 0h24v24H0V0z" fill="none"/></g><g><path d="M12 17.27 18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21 12 17.27z"/></g></svg>
`;
@Component({
  selector: 'perfil-profesionista',
  templateUrl: './perfil-profesionista.component.html',
  styleUrls: ['./perfil-profesionista.component.css']
})
export class PerfilProfesionistaComponent implements OnInit,OnDestroy{
  @ViewChild(GraficoBarraResenasComponent) chartComponent: GraficoBarraResenasComponent | undefined;
  protected readonly Array = Array;
  private subscription:Subscription | undefined;
  mensaje:MensajeRespuesta = {} as MensajeRespuesta;
  profesionista: Profesionista = {
    id: 0,
    suscrito: false,
    persona: {
      apellido: '',
      nombre: '',
      telefono1: ''
    } as Persona,
    comunicacionWsp: false,
    presentacion: '',
    poseeMatricula: false,
    nroMatricula: '',
    profesiones: [],
    promedioResenias:0
  };
  cliente:Cliente = {} as Cliente;
  reseniaStats: ReseniaStats = {} as ReseniaStats;
  resenias:Resenia[] = [];
  profesionistaId:number = 0;
  reseniaForm: FormGroup = this.fb.group({});
  stars = Array(5).fill(0);
  nuevaResena:Boolean = true;
  constructor(private profesionistasService:ProfesionistasService,
              private usuarioService:UsuarioService,
              private clienteService:ClientesService,
              private route: ActivatedRoute,
              iconRegistry: MatIconRegistry,
              sanitizer: DomSanitizer,
              private alerta: MatSnackBar,
              private fb: FormBuilder) {
    iconRegistry.addSvgIconLiteral('estrella-llena', sanitizer.bypassSecurityTrustHtml(SVG_STAR));
    iconRegistry.addSvgIconLiteral('estrella-vacia', sanitizer.bypassSecurityTrustHtml(SVG_STAR_VACIA));
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }

  ngOnInit() {
    this.subscription = new Subscription();

    this.nuevaResena = this.usuarioService.estaLogueado() && this.usuarioService.rolUsuario() == Roles.CLIENTE;

    this.reseniaForm = this.fb.group({
      descripcion: ['', Validators.required],
      calificacion: [0, [Validators.required, Validators.min(1)]]
    });

    this.route.params.subscribe({
      next: (params: Params) => {
        this.profesionistaId = params['id'];
      }
    })

    this.subscription.add(
      this.profesionistasService.getProfesionistaById(this.profesionistaId).subscribe({
        next: (response:Profesionista)=>{
           this.profesionista = response;
        },
        error:()=>{
          alert("Error al encontrar al profesionista");
        }
      })
    )

    if (this.usuarioService.rolUsuario() == Roles.CLIENTE){
      this.subscription.add(
        this.clienteService.getClienteByUserEmail(this.usuarioService.getUsuarioLogueado().email).subscribe({
          next: (response)=>{
            this.cliente = response;
          },
          error:()=>{
            alert("Error al encontrar al cliente logueado");
          }
        })
      )
    }

    this.obtenerResenias(this.profesionistaId);
    this.obtenerStatsResenias(this.profesionistaId);
  }
  openSnackBar(mensaje:string) {
    this.alerta.open(mensaje,"Cerrar",{duration:3000});
  }

  guardarResenia() {
    const reseniaDTO:ReseniaDTO = {
      descripcion: this.descripcion?.value,
      calificacion: this.calificacion?.value,
      clienteId: this.cliente.id,
      profesionistaId: this.profesionistaId
    }
    this.subscription?.add(
      this.profesionistasService.postResenia(reseniaDTO).subscribe({
        next:(response) => {
          this.mensaje = response;
          this.obtenerResenias(this.profesionistaId);
          this.obtenerStatsResenias(this.profesionistaId);
          if (this.chartComponent) {
            this.chartComponent.recargarDatos();
          }
          this.reseniaForm.reset();
          this.openSnackBar(this.mensaje.mensaje);
        },
        error:(response) => {
          this.mensaje = response;
          this.openSnackBar(this.mensaje.mensaje);
        }
      })
    )
  }

  private obtenerResenias(profesionistaId: number) {
    this.subscription?.add(
      this.profesionistasService.getReseniasByProfesionista(profesionistaId).subscribe({
        next:(response)=>{
          this.resenias = response;
        },
        error:()=>{
          alert("Error al encontrar al profesionista");
        }
      })
    )
  }

  obtenerStatsResenias(profesionistaId:number){
    this.subscription?.add(
      this.profesionistasService.getReseniasStatsByProfesionista(profesionistaId).subscribe({
        next: (response: ReseniaStats) => {
          if (response) {
            this.reseniaStats = response;
          }
        },
        error: () => {
          alert('Error al encontrar las estadísticas de reseñas');
        }
      })
    );
  }

  get calificacion(){
    return this.reseniaForm.get('calificacion');
  }
  get descripcion(){
    return this.reseniaForm.get('descripcion');
  }
}
