import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GraficoTurnosMesComponent } from './grafico-turnos-mes.component';

describe('GraficoTurnosMesComponent', () => {
  let component: GraficoTurnosMesComponent;
  let fixture: ComponentFixture<GraficoTurnosMesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GraficoTurnosMesComponent]
    });
    fixture = TestBed.createComponent(GraficoTurnosMesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
