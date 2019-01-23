var data = {
    input_devices: {},
    output_devices: {}
}

function set_trigger(idev, odev, trigger) {
    input_devices[idev].triggers[odev] = trigger;
}

function submit() {
    // TODO: post modification to set_trigger
}

devices = function() {
    var vm = new Vue({
        el: '#root',
        data: data,
        methods: {
            setTrigger: set_trigger,
            submitModification: submit
        }
    })

    $.get("/get_devices", function(res) {
        res_obj = JSON.parse(res);
        data.input_devices = res_obj[0];
        data.output_devices = res_obj[1];
    })
}

