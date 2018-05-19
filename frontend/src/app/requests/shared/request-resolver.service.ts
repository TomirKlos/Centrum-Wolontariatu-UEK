import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

import { VolunteerRequestVM } from '../../shared/interfaces';
//import { ServerResolverService } from '../../shared/server-resource.service';
import { RequestService } from '../shared/request.service';

@Injectable()
export class ViewRequestResolverService //extends ServerResolverService<VolunteerRequestVM> 
{

  constructor(private _requestService: RequestService, private _router: Router) {
    //super(_requestService, _router);
  }
}
