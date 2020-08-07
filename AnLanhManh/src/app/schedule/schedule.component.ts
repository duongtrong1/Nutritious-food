import { Component, OnInit, Inject } from '@angular/core';
import axios from "axios";
import { LOCAL_STORAGE, WINDOW } from '@ng-toolkit/universal';
import { environment } from '../../environments/environment';
import { ActivatedRoute } from '@angular/router';
import { UtilService } from '../services/util.service';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-schedule',
  templateUrl: './schedule.component.html',
  styleUrls: ['./schedule.component.css']
})
export class ScheduleComponent implements OnInit {

  constructor(@Inject(WINDOW) private window: Window, @Inject(LOCAL_STORAGE) private localStorage: any,
  private route: ActivatedRoute,
  public util: UtilService,
  private titleService: Title
  ) {
    
   }

  token: any = this.localStorage.getItem('token');

  ngOnInit() {
    this.titleService.setTitle('Lộ trình ăn uống | AnLanhManh.Com');
    this.loadSchedule();
  }

  dataSchedule : any;
  id: any;
  public loadSchedule: Function = () => {
    // this.id = this.util.getIDfromURL(this.route.snapshot.params['id']);
    const that = this;
    axios.get(`${environment.api_url}/api/suggest/schedule`, { headers: { Authorization: that.token } })
      .then(function (response) {
        if(response.data.status == 200){
        that.dataSchedule = response.data.data;
        console.log(that.dataSchedule );
        }
      })
      .catch(function (error: any) {
        // handle error
        console.log(error);
      });
  }

  getScheduleDetail(){}
}
