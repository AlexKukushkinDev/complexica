import { Component, ViewChild } from '@angular/core';
import { Location } from '@angular/common';
import { NgForm, FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { FormControl, FormGroup } from '@angular/forms';
import { custom } from 'devextreme/ui/dialog';
import { AppInfoService } from '../../shared/services/app-info.service'

// import { environment } from '../../../../environments/environment.prod';
// import 'rxjs/add/operator/map';
// import 'rxjs/add/operator/catch';
// import 'rxjs/add/observable/throw';

@Component({
  templateUrl: 'forecast.component.html',
  styleUrls: [ './forecast.component.scss' ]
})

export class ForecastComponent {
  dataSource: any;
  colCountByScreen: object;
  @ViewChild('form') form: NgForm;


  public forecastData: any = [];
  public agreementsSource: any = [];
  public formValuesChangeSubscription: any = null;

  protected result: any = null;  

  constructor(
    protected router: Router,
    protected route: ActivatedRoute,
    protected location: Location,
    public appInfoService: AppInfoService
  ) {
    this.dataSource = {
      cityName: '',
      weatherDate: new Date(),
    };
    this.colCountByScreen = {
      xs: 1,
      sm: 2
    };
    this.location = location;
  }


  ngOnInit() {
    // some code here;
  }

  onFieldChanged(event) {
    console.log(event);

    if (event && event.datafield == 'cityName') {
      this.dataSource.cityName = event.value;
    } else if (event && event.datafield == 'weatherDate') {
      this.dataSource.weatherDate = event.value;
    } 
  }

  onSubmit() {
    return this.appInfoService.submitData(this.dataSource)
        .then((response: any) => {
            if (response) {
              console.log(response);
                this.showForecastInfo(response);
            }   
        })
        .catch((error) => {
          console.log('VALIDATE ERROR: ', error);

          if (error && error.message) {
            if (error.message.includes("TMS Server not responding")) {
            } else if (error.statusCode == 400) {
              this.showErrorMessage(error);   
            } else { 
              this.showErrorMessage(error);
            }
          }
      });
  }

  generateSummary() {
    return this.appInfoService.generateSummary(this.dataSource)
        .then((response: any) => {
            if (response) {
              console.log(response);
                let weatherMessage = null;
                const todayDate = new Date().toISOString().slice(0,10);

                let generatedSummary = JSON.parse(response);

                for (let i = 0; i < generatedSummary.length; i++) {
                    if (generatedSummary[i].date === todayDate) {
                      weatherMessage = generatedSummary[i].weatherDescription;

                      weatherMessage += `.`;
                      if (parseInt(generatedSummary[i].temperature[0]) < 5) {
                        weatherMessage += `<br>It's cold. Please take a coat!`;
                      }
                    }
                }

                if (weatherMessage.toLowerCase().includes('rain')) {
                    weatherMessage += `<br>It's raining. You should take an umbrella!`;
                } 
                
                this.showGeneratedSummary(weatherMessage);
            }   
        })
      .catch((error) => {
          console.log('VALIDATE ERROR: ', error);

          if (error && error.message) {
            if (error.message.includes("TMS Server not responding")) {
            } else if (error.statusCode == 400) {
              this.showErrorMessage(error);   
            } else { 
              this.showErrorMessage(error);
            }
          }
      });
  }

  /* Refresh form and set default values depending on the selected data type */
  refreshForm(type) {
    return new Promise((resolve, reject) => {

    if (type) {
        this.dataSource = new FormGroup({
          cityName: new FormControl(),
          weatherdate: new FormControl()
        });
        resolve(true);
      }
    });
  }

  informUser(oldType, newType) { 
    return new Promise((resolve, reject) => {
      let myDialog = custom({
        title: "Warning",
        messageHtml: "Do you really want to change the selected type?<br>All data for the current type will be lost.",
        buttons: [{
          text: "Yes",
          onClick: (e) => {
            this.refreshForm(newType);
            return { buttonText: e.component.option("text") }
          }
        }, {
          text: "No",
          onClick: (e) => {
            this.unsubscribeFormValuesChanges();
            this.dataSource.patchValue({ type: oldType });
            return { buttonText: e.component.option("text") }
          }
        }]
      });
      myDialog.show();
      resolve(true);
    });
  }


  unsubscribeFormValuesChanges() {
    if (this.formValuesChangeSubscription) {
      this.formValuesChangeSubscription.unsubscribe();
    }
  }

  ngOnDestroy() {
    this.unsubscribeFormValuesChanges();
  }

  // Helpers
  // =========================

  showErrorMessage(error) {
    if (error && error.message && error.trackingId) {
      let errorMessage = custom({
        showTitle: false,
        messageHtml: error && error.message ? 
        `<div class="support">
            <b style="font-size:16px; display: flex; justify-content: center; border-bottom: 1px solid lightgray;">
              Error - Ref ID: ${error.trackingId}
            </b><br><br>
              ${error.message.split(".", 2)}.
            <br><br><br>
            <b style="font-size:12px; display: flex; justify-content: center;">For assistance please contact alex_kukushkin@support.com</b>
        </div>` 
        : `Please contact your System Administrator.`,
        
        buttons: [{
          text: "Dismiss",
          onClick: (e) => {
            return { buttonText: e.component.option("text") }
          }
        }]
      });
      errorMessage.show();
    } else if (error && error.message && !error.trackingId) {
      let errorMessage = custom({
        showTitle: false,
        messageHtml: error && error.message ? 
        `<div class="support">
            <b style="font-size:16px; display: flex; justify-content: center; border-bottom: 1px solid lightgray;">
              Error
            </b>
            <br><br>
            <div style="font-size:14px; display: flex; justify-content: center;">
              ${error.message.split(".", 2)}.
            </div>
            <br><br>
            <b style="font-size:12px; display: flex; justify-content: center;">For assistance please contact alex_kukushkin@support.com</b>
        </div>` 
        : `Please contact your System Administrator.`,
        
        buttons: [{
          text: "Dismiss",
          onClick: (e) => {
            return { buttonText: e.component.option("text") }
          }
        }]
      });
      errorMessage.show();
    }
  }

  showForecastInfo(data) {
    this.appInfoService.setForecastSource(data);
    this.router.navigate(['itineraries']);
  }

  showGeneratedSummary(info) {
    let message = custom({
      title: "Weather Summary",
      messageHtml: info ? `The weather today is - ${info}` : `The weather if fine today. Enjoy the rest of the day!`,
      buttons: [{
        text: "Ok",
        onClick: (e) => {
          return { buttonText: e.component.option("text") }
        }
      }]
    });
    message.show();
  }

  goNext() {
    // this.prepareTable();
  }

  switchToStep(step) {
    this.router.navigate([step]);
  }

  isEmpty(obj) {
    for(let prop in obj) {
        if(obj.hasOwnProperty(prop))
            return false;
    }
    return true;
  }
}
