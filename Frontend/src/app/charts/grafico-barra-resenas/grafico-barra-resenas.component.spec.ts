import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GraficoBarraResenasComponent } from './grafico-barra-resenas.component';

describe('GraficoBarraResenasComponent', () => {
  let component: GraficoBarraResenasComponent;
  let fixture: ComponentFixture<GraficoBarraResenasComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GraficoBarraResenasComponent]
    });
    fixture = TestBed.createComponent(GraficoBarraResenasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
