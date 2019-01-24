var data = {
    input_devices: {},
    output_devices: {}
}

function set_trigger(idev, odev, trigger) {
    data.input_devices[idev].triggers[odev] = trigger;
}

function submit() {
    res = {};
    for (var idev_id in data.input_devices) {
        res[idev_id] = data.input_devices[idev_id].triggers;
    }
    res_str = JSON.stringify(res);
    console.log(res_str);
    $.post("./set_trigger", "triggers=" + res_str);
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

