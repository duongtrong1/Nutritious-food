import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AteComponent } from './ate.component';

describe('AteComponent', () => {
  let component: AteComponent;
  let fixture: ComponentFixture<AteComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AteComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
