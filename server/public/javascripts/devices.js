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
        computed: {
            has_online_idevs: function() {
                for (var idev_id in this.input_devices) {
                    if (this.input_devices[idev_id].online) {
                        return true;
                    }
                }
                return false;
            },
            has_online_odevs: function() {
                for (var odev_id in this.output_devices) {
                    if (this.output_devices[odev_id].online) {
                        return true;
                    }
                }
                return false;
            }
        },
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

