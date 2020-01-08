(function () {
    getYearlyInflow();
  })();

var months = ['💦', 'jan', 'feb', 'mar', 'apr', 'may', 'june', 'july', 'aug', 'sep', 'oct', 'nov', 'dec'];
var monthsRow = document.getElementById('months');
var container = document.getElementById('inflow-chart');
var monthRowHTML = [];

//Creating the months row
months.forEach((e) => {
    monthRowHTML.push(`<div class="stat-box"><p class="my-flow-text" style="transform:rotate(-60deg); color:white">${e}</p></div>`);
});
monthsRow.innerHTML = monthRowHTML.join("");

//Adds a row with stats for each years inflow
function addRow(year, data, container, scale) {
    //Add year row
    var newRow = document.createElement('div');
    newRow.classList.add('year-row');

    //Add boxes into row
    var currentRowHTML = [];
    //Adding Year box
    currentRowHTML.push(`<div class="stat-box"><p class="my-flow-text" style="transform:rotate(-60deg); color:white">${year}</p></div>`);
    //Adding stat boxes for each month of the year
    data.forEach((number) => {
        var scaleForClass = Math.round((number / scale[1]) * 10) / 10;
        var roundedNumber = Math.round(number * 10) / 10;
        currentRowHTML.push(`<div class="stat-box stat-box-active ${getClass(scaleForClass)}"><p class="my-flow-text" >${roundedNumber}</p></div>`);
    });
    newRow.innerHTML = currentRowHTML.join("");
    container.appendChild(newRow);
}

//Returns a coloring class depending on a number scale
function getClass(number) {
    switch (number) {
        case 0.1:
            return 'point-one';
            break;
        case 0.2:
            return 'point-two';
            break;
        case 0.3:
            return 'point-three';
            break;
        case 0.4:
            return 'point-four';
            break;
        case 0.5:
            return 'point-five';
            break;
        case 0.6:
            return 'point-six';
            break;
        case 0.7:
            return 'point-seven';
            break;
        case 0.8:
            return 'point-eight';
            break;
        case 0.9:
            return 'point-nine';
            break;
        case 1:
            return 'one';
            break;
        default:
            return 'point-one';
            break
    }
}

//Take in the data from the inflow API
function getYearlyInflow() {
    fetch(API_INFLOW)
        .then((resp) => resp.json())
        .then(function (data) {
            //Getting the scale that comes from the largest and smallest inflows of all years
            var scale = getScale(data);
            if (scale === null || scale === undefined) {
                console.log('Error getting scale');
            } else {
                //Creating rows for each year's stats
                for (var year in data) {
                    addRow(year, data[year], container, scale);
                }
            }
        })
        .catch(function (error) {
            console.log(error);
        });
}

//Given the year data it returns the smallest and largest stat of inflow
function getScale(years) {
    var allValues = [];

    //Collecting all stats of all years
    for (var year in years) {
        var values = years[year];
        for (var value in values) {
            allValues.push(values[value]);
        }
    }
    //Sorting the stats
    allValues.sort(function (a, b) { return a - b; });

    //Extract the smallest and largest values
    if (allValues.length > 2) {
        return [allValues[0], allValues[allValues.length - 1]];
    } else {
        return null;
    }
}