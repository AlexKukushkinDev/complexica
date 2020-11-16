import { Injectable } from '@angular/core';
import { environment as env } from './../../../environments/environment';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';
import 'rxjs/add/operator/switchMap';

import { custom } from 'devextreme/ui/dialog';
import { errorHandler } from '../../../app/shared/components/error-handler'
import { HttpErrorResponse, HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { catchError, map, timeout } from 'rxjs/operators';


const API_URL = env.url;

@Injectable()
export class AppInfoService {
  public defaultHeaders = new HttpHeaders();

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
    let headers = this.defaultHeaders;
    let contentType = 'application/json';

    debugger
    formData.append('cityName', body.cityName);
    formData.append('weatherDate', body.weatherDate);

    const httpHeaderAccepts: string[] = ["application/json"];

    return this.http.post(requestLink, formData,{ responseType: 'text' }).pipe(
        timeout(1000),
        map(res => {
            debugger
            return res;
        }),
        catchError((error) => { return errorHandler(error); })
      ).toPromise();
  }

  generateSummary(body: any) {
    let requestLink = `${API_URL}/summary`;

    const formData = new FormData();
    let headers = this.defaultHeaders;
    let contentType = 'application/json';

    debugger
    formData.append('cityName', body.cityName);
    formData.append('weatherDate', body.weatherDate);

    const httpHeaderAccepts: string[] = ["application/json"];

    return this.http.post(requestLink, formData,{ responseType: 'text' }).pipe(
        timeout(1000),
        map(res => {
            debugger
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

}
