import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AppInfoService } from '../../shared/services/app-info.service';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/distinctUntilChanged';
import 'devextreme/data/odata/store';

@Component({
  templateUrl: 'itineraries.component.html'
})

export class ItinerariesComponent {
  forecastDataSource: any[];
  priority: any[];

  constructor(
    protected router: Router,
    protected route: ActivatedRoute,
    public appInfoService: AppInfoService
  ) {
    this.priority = [
      { name: 'High', value: 4 },
      { name: 'Urgent', value: 3 },
      { name: 'Normal', value: 2 },
      { name: 'Low', value: 1 }
    ];
  }

  ngOnInit() {
    this.showForecastGrid();
  }

  showForecastGrid() {
    this.appInfoService.forecastInfo.debounceTime(150).distinctUntilChanged().subscribe((result) => {
      let data = result;

      if (result) {
        data = JSON.parse(result);
        this.appInfoService.setForecast(data);
        this.forecastDataSource = this.appInfoService.getForecast();
      }
  });
  }

  ngOnDestroy() {
    // unsubscribe subscriptions
  }
}
