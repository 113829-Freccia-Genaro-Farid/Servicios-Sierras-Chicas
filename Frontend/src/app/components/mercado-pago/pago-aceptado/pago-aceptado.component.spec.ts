import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PagoAceptadoComponent } from './pago-aceptado.component';

describe('PagoAceptadoComponent', () => {
  let component: PagoAceptadoComponent;
  let fixture: ComponentFixture<PagoAceptadoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PagoAceptadoComponent]
    });
    fixture = TestBed.createComponent(PagoAceptadoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
