
export function  timeFormat(value) {
    let dateee = new Date(value).toJSON();
    let date = new Date(+new Date(dateee) + 8 * 3600 * 1000).toISOString()
      .replace(/T/g, ' ')
      .replace(/\.[\d]{3}Z/, '')
    return date;
}

export function  dateFormat(value) {
   let date = timeFormat(value);
   return date.split(" ")[0];
}

export function  monthDayFormat(value) {
  let date = timeFormat(value);
  let split = date.split(" ")[0].split('-');
  return split[1]+"-"+split[2];
}


