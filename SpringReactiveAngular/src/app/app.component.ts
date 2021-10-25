import { ChangeDetectorRef, Component } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Person } from "./model/person";
import { PersonEvent } from "./model/person-event";
import { PersonServiceService } from "./services/person-service.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'SpringReactiveAngular';

  persons: Person[] = []
  personEvent!: PersonEvent;
  environmentName = environment.environmentName;
  apiBackEndUrl = environment.apiBackEndUrl;

  readonly personEventUrl: string = this.apiBackEndUrl + "/persons/1/events";
  readonly personUrl: string = this.apiBackEndUrl + "/persons";
  readonly personStreamUrl1: string = this.apiBackEndUrl + "/persons/stream1";
  readonly personStreamUrl2: string = this.apiBackEndUrl + "/persons/stream2";
  

  constructor(private personServiceService: PersonServiceService, private cdr: ChangeDetectorRef) {
    
  }

  ngOnInit(): void {

    console.log("Environment Name is " + this.environmentName);
    console.log("BackEnd API is is " + this.apiBackEndUrl);

    //this.getPersonList();
    this.observePersons1();
    //this.observePersons2();

    //this.getPersonEvent();

  }

  getPersonList() {
    this.personServiceService.getPersons(this.personUrl).subscribe(responseData => {
      console.log(responseData);
      this.persons = responseData;
    });
  }

  getPersonEvent() {
    this.personServiceService.getPersonEvent(this.personEventUrl).subscribe(responseData => {
      this.personEvent = responseData;
    });
  }

  observePersons1() {
    this.personServiceService.observePersons1(this.personStreamUrl1)
      .subscribe((person: Person) => {
        console.log(person);
        this.persons.push(person);
      });
  }

  observePersons2() {
    this.personServiceService.observePersons2(this.personStreamUrl2)
      .subscribe((person: Person) => {
        console.log(person);
        this.persons.push(person);
        this.cdr.detectChanges();
      });
  }

}


