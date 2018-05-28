import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

import { VolunteerRequestVM } from '../../shared/interfaces';
//import { ServerResolverService } from '../../shared/server-resource.service';
import { AdService } from './ad.service';

@Injectable()
export class ViewAdResolverService //extends ServerResolverService<VolunteerRequestVM> 
{

  constructor(private _adService: AdService, private _router: Router) {
    //super(_requestService, _router);
  }
}
