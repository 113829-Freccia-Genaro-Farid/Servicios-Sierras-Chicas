import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GraficoTortaSubsComponent } from './grafico-torta-subs.component';

describe('GraficoTortaSubsComponent', () => {
  let component: GraficoTortaSubsComponent;
  let fixture: ComponentFixture<GraficoTortaSubsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GraficoTortaSubsComponent]
    });
    fixture = TestBed.createComponent(GraficoTortaSubsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
