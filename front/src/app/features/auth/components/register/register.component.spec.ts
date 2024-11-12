import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { expect } from '@jest/globals';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { RegisterComponent } from './register.component';
import { of } from 'rxjs';
import { RouterTestingModule } from '@angular/router/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let mockAuthService: any;
  let mockRouter: any;

  beforeEach(async () => {
    mockAuthService = {
      register: jest.fn()
    };

    mockRouter = {
      navigate: jest.fn()
    };

    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter }],
      imports: [ReactiveFormsModule,
                BrowserAnimationsModule,
                MatCardModule,
                MatFormFieldModule,
                MatIconModule,
                MatInputModule]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });
  
  it('should create', () => {
    expect(component).toBeTruthy();
  });
  
  it('should initialize form', () => {
    fixture.detectChanges();
    
    const form = component.form;
    expect(form.contains('email')).toBe(true);
    expect(form.contains('firstName')).toBe(true);
    expect(form.contains('lastName')).toBe(true);
    expect(form.contains('password')).toBe(true);

  });

  it('should register and navigate to /login', () => {
    const mockResponse = {
      email: 'john@gmail.com',
      firstName: "john",
      lastName: "doe",
      password: 'password'
    };

    mockAuthService.register.mockReturnValue((of(mockResponse)));

    component.form.setValue({email: 'john@gmail.com',
                            firstName: "john",
                            lastName: "doe",
                            password: 'password'});

    component.submit();

    expect(mockAuthService.register).toHaveBeenCalledWith({email: 'john@gmail.com',
                                                          firstName: "john",
                                                          lastName: "doe",
                                                          password: 'password'});


    expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
  });
});
