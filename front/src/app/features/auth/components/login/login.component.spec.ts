import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { AuthService } from '../../services/auth.service';
import { LoginComponent } from './login.component';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';


describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let mockAuthService: any;
  let mockSessionService: any;
  let mockRouter: any;

  beforeEach(async () => {
    mockAuthService = {
      login: jest.fn()
    };

    mockSessionService = {
      logIn: jest.fn()
    };

    mockRouter = {
      navigate: jest.fn()
    };

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: Router, useValue: mockRouter }],
      imports: [ReactiveFormsModule]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
  });
  
  it('should create', () => {
    expect(component).toBeTruthy();
  });
  
  it('should initialize form', () => {
    fixture.detectChanges();

    const form = component.form;
    expect(form.contains('email')).toBe(true);
    expect(form.contains('password')).toBe(true);
  });

  it('should log in and navigate to /sessions', () => {
    const mockResponse = {
      id: 1,
      username: 'john_doe'
    };

    mockAuthService.login.mockReturnValue(of(mockResponse));

    component.form.setValue({email: 'john@gmail.com', password: 'password'});

    component.submit();

    expect(mockAuthService.login).toHaveBeenCalledWith({email: 'john@gmail.com', password: 'password'});

    expect(mockSessionService.logIn).toHaveBeenCalledWith(mockResponse);

    expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('should set onError to true if login fails', () => {
    mockAuthService.login.mockReturnValue(throwError(() => new Error('Login failed'))); // Simule une erreur

    component.form.setValue({ email: 'john@example.com', password: 'password123' });

    component.submit();

    expect(mockAuthService.login).toHaveBeenCalled();

    expect(component.onError).toBe(true);
  });
});
