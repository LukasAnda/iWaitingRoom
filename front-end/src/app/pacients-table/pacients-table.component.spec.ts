import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PacientsTableComponent } from './pacients-table.component';

describe('PacientsTableComponent', () => {
  let component: PacientsTableComponent;
  let fixture: ComponentFixture<PacientsTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PacientsTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PacientsTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
