var form = (function(){
    var self = this;
    self.domain = document.location.origin+"/jbook";
    self.client;
    self.list = [];
    self.tpl = '<tr><td>#surname#</td>' +
        '<td>#firstname#</td>' +
        '<td>#middlename#</td>' +
        '<td>#birthday#</td>' +
        '<td>#email#</td>' +
        '<td>#phone#</td>' +
        '<td><button class="btn btn-info action" data-action="edit" data-attr="#identifier#">изменить</button></td>' +
        '<td><button class="btn btn-warning action" data-action="rm" data-attr="#identifier#">удалить</button></td>' +
        '</tr>';
    self.init = function(){
        var client = self.getCookie('client');
        if(client != undefined) {
            self.client = JSON.parse(client);
            self.loadData();
        }
        else {
            $('#bookList').addClass("hidden");
            $('#auth').modal('show');
            $('#auth-login').val('');
            $('#auth-pass').val('');
        }
        $('#auth').on('hidden.bs.modal', function (e) {
            if(self.client == undefined || self.client == null){
                setTimeout(function(){
                    self.exit();
                },500);
            }
        });
        $(document).on('click','.action',function(){
            var el = $(this).data('action');
            var attr = $(this).data('attr');
            if( typeof form[el]=='function'){
                form[el](attr);
            }
        });
    };
    self.eClient = {};
    self.save = function(){
        for(var el in self.eClient){
            if(el=="identifier") continue;
            var _this = $('#'+el);
            if(_this!=null && typeof _this.val == 'function'){
                self.eClient[el] = _this.val();
            }
        }
        self.post(self.domain+'/usr/add',JSON.stringify(self.eClient),function(){
            self.loadData();
        });
    };
    self.add = function(){
        $('#edit').modal('show');
        self.eClient = {
            surname: null,
            firstname: null,
            middlename: null,
            login: null,
            password: null,
            birthday: null,
            email: null,
            phone: null
        };
        $('#edit form')[0].reset();
        $('#edit h4').text('Новая запись');
        $('#login').attr('disabled',false);
        $('#show-password').removeClass("hidden");
    };
    self.edit = function(id){
        $('#edit').modal('show');
        $('#edit form')[0].reset();
        var map = null;
        for(var el in self.list){
            if(self.list[el].identifier==id){
                map = self.list[el];
                break;
            }
        }
        if(map!=null){
            self.eClient = map;
            $('#login').attr('disabled',true);
            $('#show-password').addClass("hidden");
            var name = map.surname+" "+map.firstname+" "+map.middlename;
            $('#edit h4').text(name);
            for(var el in map){
                var _this = $('#'+el);
                if(_this!=null && typeof _this.val == 'function'){
                    _this.val(map[el]);
                }
            }
        }
    };
    self.rm = function(id){
        if(confirm("Вы уверены?")){
            self.get(self.domain+'/rm/'+id,'',function(){
                self.loadData();
            });
        };
    };
    self.login = function(){
        var auth = {
            login: $('#auth-login').val(),
            pass: $('#auth-pass').val()
        }
        self.post(self.domain+'/auth/token',JSON.stringify(auth),function(resp){
            if(resp.error != undefined) {
                $('#auth-error').removeClass("hidden").text(resp.data);
                return;
            }
            self.client = resp.data;
            self.client.token = resp.request.getResponseHeader("Authorization");
            self.setCookie("client",JSON.stringify(self.client),{expires:3600, path:"/"});
            $('#authInfo h4').html(self.client.surname+" "+self.client.firtsname+" "+self.client.middlename+" <strong class='action' data-action='exit'>Выход</strong>")
            $('#auth').modal('hide');
            $('#bookList').removeClass("hidden");
            self.loadData();
        });
    };
    self.clearData = function(){
        $('#find').val('');
        self.loadData();
    };
    self.loadData = function(){
        self.post(self.domain+'/usr',JSON.stringify({find: $('#find').val()}),function(resp){
            console.log(resp);
            $('#bookListTable').text('');
            if(resp.error != undefined){
                if(resp.data == "Ошибка авторизации"){
                    self.exit();
                } else {
                    $('#load-error').removeClass("hidden").text(resp.data);
                }
                return;
            }
            var list = [];
            self.list = resp.data;
            for(var el in self.list){
                var elContent = self.tpl;
                for(var sub in self.list[el]){
                    elContent = elContent.split("#"+sub+"#").join(self.list[el][sub]);
                }
                list.push(elContent);
            }
            $('#bookListTable').html(list.join(''));

        });
    };
    self.ajax = function(url,type,data,callback){
        var headers = {'Content-Type':'application/json; charset=UTF-8'};
        if(self.client != null && self.client.token!=undefined)
            headers.Authorization = self.client.token;
        $.ajax({
            headers: headers,
            url: url,
            type: type,
            data: data,
            success: function (result,textStatus,resp) {
                console.log("success",textStatus,result,resp);
                callback({success: true, data: result, request: resp});
            },
            error: function (result,textStatus) {
                console.log("error",textStatus,result);
                callback({error: true, data: result.responseText, request: result});
            },
            complete: function(xhr,status){
                console.log("complete",status,xhr);
            }
        });
    }
    self.get = function(url,data,callback){
        self.ajax(url,'GET',data,callback);
    };
    self.post = function(url,data,callback){
        self.ajax(url,'POST',data,callback);
    };
    self.exit = function(){
        self.client = null;
        $('#bookList').addClass("hidden");
        $('#auth').modal('show');
        $('#auth-login').val('');
        $('#auth-pass').val('');
    };
    self.getCookie = function(name) {
        var matches = document.cookie.match(new RegExp(
            "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
        ));
        return matches ? decodeURIComponent(matches[1]) : undefined;
    };
    self.setCookie = function(name, value, options) {
        options = options || {};

        var expires = options.expires;

        if (typeof expires == "number" && expires) {
            var d = new Date();
            d.setTime(d.getTime() + expires * 1000);
            expires = options.expires = d;
        }
        if (expires && expires.toUTCString) {
            options.expires = expires.toUTCString();
        }

        value = encodeURIComponent(value);

        var updatedCookie = name + "=" + value;

        for (var propName in options) {
            updatedCookie += "; " + propName;
            var propValue = options[propName];
            if (propValue !== true) {
                updatedCookie += "=" + propValue;
            }
        }

        document.cookie = updatedCookie;
    };
    return self;
})();
