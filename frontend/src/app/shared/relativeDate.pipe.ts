import { Pipe, PipeTransform } from '@angular/core';

// Epochs
const epochs: any = [
    ['year', 31536000],
    ['month', 2592000],
    ['day', 86400],
    ['hour', 3600],
    ['minute', 60],
    ['second', 1]
];


@Pipe({name: 'relativeDatePipe'})
export class RelativeDatePipe implements PipeTransform {

    getDuration(timeAgoInSeconds: number) {
        for (let [name, seconds] of epochs) {
            let interval = Math.floor(timeAgoInSeconds / seconds);

            if (interval >= 1) {
                return {
                    interval: interval,
                    epoch: name
                };
            }
        }
        return {
            interval: 0,
            epoch: 'seconds'
        };
    };

    transform(dateStamp: number): string {

        let timeAgoInSeconds = Math.floor((new Date().getTime() - new Date(dateStamp).getTime()) / 1000);
        let {interval, epoch} = this.getDuration(timeAgoInSeconds);
        //let suffix = interval === 1 ? '' : 's';
        let suffix;

        if(timeAgoInSeconds/31536000>1){
            suffix = interval === 1 ? 'rok' : 'lat';
        } else if(timeAgoInSeconds/2592000 > 1){
            suffix = interval === 1 ? 'miesiąc' : 'miesięcy';
        } else if(timeAgoInSeconds/86400 > 1){
            suffix = interval === 1 ? 'dzień' : 'dni';
        } else if(timeAgoInSeconds/3600 > 1){
            suffix = interval === 1 ? 'godzinę' : 'godzin';
        } else if(timeAgoInSeconds/60 > 1){
            suffix = interval === 1 ? 'minutę' : 'minut';
        } else if(timeAgoInSeconds/1 > 1){
            suffix = interval === 1 ? 'sekundę' : 'sekund';
        }

        return `${interval} ${suffix} temu`;

    }

}

@Pipe({name: 'relativeDatePipeExpiration'})
export class RelativeDatePipeExpiration implements PipeTransform {

  getDuration(timeAgoInSeconds: number) {
    for (let [name, seconds] of epochs) {
      let interval = Math.floor(timeAgoInSeconds / seconds);

      if (interval >= 1) {
        return {
          interval: interval,
          epoch: name
        };
      }
    }
    return {
      interval: 0,
      epoch: 'seconds'
    };
  };

  transform(dateStamp: number): string {

    let timeAgoInSeconds = Math.floor((new Date(dateStamp).getTime() - new Date().getTime()) / 1000);
    let {interval, epoch} = this.getDuration(timeAgoInSeconds);
    //let suffix = interval === 1 ? '' : 's';
    let suffix;

    if(timeAgoInSeconds/31536000>1){
      suffix = interval === 1 ? 'rok' : 'lat';
    } else if(timeAgoInSeconds/2592000 > 1){
      suffix = interval === 1 ? 'miesiąc' : 'miesięcy';
    } else if(timeAgoInSeconds/86400 > 1){
      suffix = interval === 1 ? 'dzień' : 'dni';
    } else if(timeAgoInSeconds/3600 > 1){
      suffix = interval === 1 ? 'godzinę' : 'godzin';
    } else if(timeAgoInSeconds/60 > 1){
      suffix = interval === 1 ? 'minutę' : 'minut';
    } else if(timeAgoInSeconds/1 > 1){
      suffix = interval === 1 ? 'sekundę' : 'sekund';
    }

    return `za ${interval} ${suffix}`;

  }

}



