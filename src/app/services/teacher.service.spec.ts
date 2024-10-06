import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { TeacherService } from './teacher.service';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { Teacher } from '../interfaces/teacher.interface';


describe('TeacherService', () => {
  let teacherService: TeacherService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers:[TeacherService]
    });

    teacherService = TestBed.inject(TeacherService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  })

  it('should be created', () => {
    expect(teacherService).toBeTruthy();
  });

  it('should get all teachers', () => {
    let teacherList: Teacher[] = [{id: 1,
                                firstName: "Monsieur",
                                lastName: "Professor Tournesol",
                                createdAt: new Date(),
                                updatedAt: new Date()},
                              {
                                id: 2,
                                firstName: "Père",
                                lastName: "Ducrass",
                                createdAt: new Date(),
                                updatedAt: new Date()
                              }];

    teacherService.all().subscribe((response) => {
      expect(response).not.toBeNull();
    })

    const request = httpMock.expectOne('api/teacher');
    expect(request.request.method).toBe('GET')

    request.flush(teacherList)
  })

  it('should not get all teachers because database is null', () => {
    teacherService.all().subscribe((response) => {
      expect(response).toBeNull();
    })

    const request = httpMock.expectOne('api/teacher');
    expect(request.request.method).toBe('GET');

    request.flush(null);
  })

  it('should get details from teacher by id', () => {
    let teacher: Teacher = {id: 1,
                                firstName: "Monsieur",
                                lastName: "Professor Tournesol",
                                createdAt: new Date(),
                                updatedAt: new Date()};
    
    teacherService.detail('1').subscribe((response) => {
      expect(response).toEqual(teacher);
    })

    const request = httpMock.expectOne('api/teacher/1');
    expect(request.request.method).toBe('GET');

    request.flush(teacher);
  })

  it('should not found details because teacherId does not exist', () => {
    teacherService.detail('1').subscribe((response) => {
      expect(response).toBeNull();
    })

    const request = httpMock.expectOne('api/teacher/1');
    expect(request.request.method).toBe('GET');

    request.flush(null);
  })
});
