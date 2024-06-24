import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {CalendarOptions, EventClickArg} from "@fullcalendar/core";
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import esLocale from '@fullcalendar/core/locales/es';
import {TurnosService} from "../../services/turnosService/turnos.service";
import {Subscription} from "rxjs";
import {Turno} from "../../models/turno";
import {FullCalendarComponent} from "@fullcalendar/angular";
import {MensajeRespuesta} from "../../models/mensaje-respuesta";
import {MatDialog} from "@angular/material/dialog";
import {AgregarTurnoComponent} from "./agregar-turno/agregar-turno/agregar-turno.component";
import {ProfesionistasService} from "../../services/profesionistasService/profesionistas.service";
import {UsuarioService} from "../../services/usuariosService/usuario.service";
import {Roles} from "../../models/Auxiliares/roles";
import {PersonasService} from "../../services/personasService/personas.service";
import {Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'turnos',
  templateUrl: './turnos.component.html',
  styleUrls: ['./turnos.component.css']
})
export class TurnosComponent implements OnInit,OnDestroy{
  @ViewChild('fullcalendar') fullcalendar: FullCalendarComponent | undefined;
  @ViewChild(AgregarTurnoComponent) dialogTurno: AgregarTurnoComponent | undefined;
  private subscription:Subscription | undefined;
  claseAnterior:string = '';
  mensaje : MensajeRespuesta = {} as MensajeRespuesta;
  turnos:Turno[] = [];
  calendarOptions: CalendarOptions = {
    plugins: [dayGridPlugin, interactionPlugin],
    initialView: 'dayGridMonth',
    selectable: true,
    selectAllow: function(selectionInfo) {
      let startDate = selectionInfo.start;
      let endDate = selectionInfo.end;
      endDate.setSeconds(endDate.getSeconds() - 1);
      return startDate.getDate() === endDate.getDate();
    },
    fixedWeekCount:false,
    titleFormat:{
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    },
    eventMouseEnter:(info)=>{
      this.claseAnterior = info.el.className;
      info.el.className += ''
    },
    eventMouseLeave:(info)=>{
      info.el.className = this.claseAnterior
    },
    titleRangeSeparator: ' al ',
    locale: esLocale,
    weekends: true,
    eventBackgroundColor:'#20ab22',
    events: [],
    eventTimeFormat:{
      hour:'numeric',
      minute:'numeric',
      omitZeroMinute: false
    },
    eventClassNames:['bg-green-600 pl-2 mt-1 text-sm select-none overflow-hidden whitespace-nowrap hover:bg-green-800 hover:text-white'],
    eventClick:(info)=> this.modificarTurno(info),
    customButtons: {
      turno: {
        text: 'Añadir Turno',
        click: () => this.openDialog({} as Turno, 0)
      }
    },
    headerToolbar: {
      start: 'prev next today',
      center: 'title',
      end: 'turno'
    }
  };

  constructor(private turnosService:TurnosService,
              private dialog: MatDialog,
              private profesionistaService:ProfesionistasService,
              private userService:UsuarioService,
              private personaService:PersonasService,
              private router:Router,
              private alerta: MatSnackBar) {
  }

  ngOnDestroy(): void {
      this.subscription?.unsubscribe();
  }

  ngOnInit(): void {
      this.subscription = new Subscription();
      this.validarProfesionista();
      this.getTurnos();
  }

  validarProfesionista(){
    if(this.userService.rolUsuario() == Roles.PROFESIONISTA){
      this.subscription?.add(
        this.personaService.getDatosPersonaByUser(this.userService.getUsuarioLogueado().email).subscribe({
          next:(response)=>{
            if(!response.habilitado){
              this.router.navigate(['/datospersonales']);
              this.alerta.open('Debes completar los datos personales para ingresar a la gestion de turnos', 'Cerrar', {duration:5000});
              setTimeout(() => {
              }, 2000);
            }
          }
        })
      )
    }

  }

  getTurnos(){
    this.subscription?.add(
      this.profesionistaService.getProfesionistaByUserEmail(this.userService.getUsuarioLogueado().email).subscribe({
        next:(prof)=>{
          this.turnosService.getTurnosByProfesionista(prof.id).subscribe({
            next:(response)=>{
              this.turnos = response;
              let turnoss:any[] = []
              for (let turno of this.turnos) {
                turnoss.push({
                  title: turno.descripcion ? '-  '+turno.descripcion : '- Turno',
                  start:turno.fechaTurno,
                  end:new Date(turno.fechaTurno).setSeconds(1),
                  id:turno.idTurno,
                })
              }
              this.calendarOptions.events = turnoss;
            },
            error:()=>{
              alert("error al cargar los turnos")
            }
          })
        }
      })
    )
  }


  modificarTurno(info:EventClickArg){
    this.subscription?.add(
      this.turnosService.getTurnosById(+info.event.id).subscribe({
        next:(response)=>{
          this.openDialog(response, 1);
        }
      })
    )
  }

  openDialog(turno:Turno, mov:number): void {
    this.dialog.open(AgregarTurnoComponent, {
      data: {
        turno : turno.idTurno != null ? turno : {} as Turno,
        mov: mov
      }
    });
    this.dialog.afterAllClosed.subscribe(result => {
      this.getTurnos();
    });
  }

}
