import { Injectable } from '@angular/core';
import { environment as env } from './../../../environments/environment';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';
import 'rxjs/add/operator/switchMap';

import { custom } from 'devextreme/ui/dialog';
import { errorHandler } from '../../../app/shared/components/error-handler'
import { HttpErrorResponse, HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { catchError, map, timeout } from 'rxjs/operators';
import { BehaviorSubject } from 'rxjs';


const API_URL = env.url;
let forecastList = [];

export class ForecastData {
  Itinerary_ID: number;
  City_Name: string;
  Date: string;
  Country_Code: string;
  Temperature: string;
  Clouds: string;
}

@Injectable()
export class AppInfoService {
  public defaultHeaders = new HttpHeaders();

  public forecastSource = new BehaviorSubject(null);

  forecastInfo = this.forecastSource.asObservable();

  constructor(
    protected http: HttpClient
  ) {
  }

  public get title() {
    return 'Travel Planner App';
  }

  public get currentYear() {
    return new Date().getFullYear();
  }

  submitData(body: any) {
    let requestLink = `${API_URL}/forecast`;

    const formData = new FormData();
    formData.append('cityName', body.cityName);
    formData.append('weatherDate', body.weatherDate);

    return this.http.post(requestLink, formData,{ responseType: 'text' }).pipe(
        timeout(60000),
        map(res => {
            return res;
        }),
        catchError((error) => { return errorHandler(error); })
      ).toPromise();
  }

  generateSummary(body: any) {
    let requestLink = `${API_URL}/summary`;

    const formData = new FormData();
    formData.append('cityName', body.cityName);
    formData.append('weatherDate', body.weatherDate);

    const httpHeaderAccepts: string[] = ["application/json"];

    return this.http.post(requestLink, formData,{ responseType: 'text' }).pipe(
        timeout(60000),
        map(res => {
            return res;
        }),
        catchError((error) => { return errorHandler(error); })
      ).toPromise();
  }

  showErrorMessage(error) {
    let errorMessage = custom({
      title: "Warning",
      messageHtml: error && error.message ? error.message : `Please contact your System Administrator.`,
      buttons: [{
        text: "Ok",
        onClick: (e) => {
          return { buttonText: e.component.option("text") }
        }
      }]
    });
    errorMessage.show();
  }

  setForecastSource(forecast: JSON) {
    this.forecastSource.next(forecast);
  }

  setForecast(data) {
    forecastList = [];

    for (let i = 0; i < data.length; i++) {
        let forecastInfo = new ForecastData();

        forecastInfo.Itinerary_ID = i + 1;
        forecastInfo.City_Name = data[i].cityName;
        forecastInfo.Country_Code = data[i].countryCode;
        forecastInfo.Date = data[i].date;
        forecastInfo.Temperature = data[i].temperature[0];
        forecastInfo.Clouds = data[i].weatherDescription;

        forecastList.push(forecastInfo);
    }
  }

  getForecast() {
      return forecastList;
  }
}
