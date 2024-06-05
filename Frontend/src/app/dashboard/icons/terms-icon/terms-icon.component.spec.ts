import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TermsIconComponent } from './terms-icon.component';

describe('TermsIconComponent', () => {
  let component: TermsIconComponent;
  let fixture: ComponentFixture<TermsIconComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TermsIconComponent]
    });
    fixture = TestBed.createComponent(TermsIconComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
