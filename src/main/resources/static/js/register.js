document.addEventListener('DOMContentLoaded', function() {
    // 获取表单元素和注册按钮
    var form = document.querySelector('form');
    var usernameInput = document.querySelector('.username');
    var emailInput = document.querySelector('.yzm');
    var passwordInput = document.querySelector('.pwd');
    var confirmPasswordInput = document.querySelector('.pwd2');
    var agreementCheckbox = document.querySelector('.fx');
    var submitButton = document.querySelector('.submit');

    // 注册按钮点击事件
    form.addEventListener('submit', function(event) {
        // 阻止表单默认提交行为
        event.preventDefault();

        // 检查用户名和密码是否为空
        if (usernameInput.value.trim() === '' || passwordInput.value.trim() === '') {
            document.getElementById('err_msg').textContent = '用户名或密码不能为空';
            return;
        }

        // 检查邮箱格式是否正确
        var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(emailInput.value.trim())) {
            document.getElementById('err_msg').textContent = '邮箱格式不正确';
            return;
        }

        // 检查两次密码是否一致
        if (passwordInput.value !== confirmPasswordInput.value) {
            document.getElementById('err_msg').textContent = '两次密码不一致';
            return;
        }

        // 检查用户协议是否勾选
        if (!agreementCheckbox.checked) {
            document.getElementById('err_msg').textContent = '请同意用户协议';
            return;
        }

        // 所有验证通过，可以提交表单
        form.submit();
    });

    // 输入字段改变时清除错误信息
    usernameInput.addEventListener('input', clearErrorMessage);
    emailInput.addEventListener('input', clearErrorMessage);
    passwordInput.addEventListener('input', clearErrorMessage);
    confirmPasswordInput.addEventListener('input', clearErrorMessage);
    agreementCheckbox.addEventListener('change', clearErrorMessage);

    // 清除错误信息
    function clearErrorMessage() {
        document.getElementById('err_msg').textContent = '';
    }
});