// Custom Dialog System for BISEN
class CustomDialog {
    static showAlert(message, title = 'Alert') {
        return new Promise((resolve) => {
            const dialog = document.createElement('div');
            dialog.className = 'custom-dialog-overlay';
            dialog.innerHTML = `
                <div class="custom-dialog">
                    <div class="custom-dialog-header">
                        <h3>${this.escapeHtml(title)}</h3>
                        <button class="custom-dialog-close" onclick="this.closest('.custom-dialog-overlay').remove()">&times;</button>
                    </div>
                    <div class="custom-dialog-body">
                        <p>${this.escapeHtml(message)}</p>
                    </div>
                    <div class="custom-dialog-footer">
                        <button class="btn btn-primary" onclick="this.closest('.custom-dialog-overlay').remove(); arguments[0].stopPropagation();">OK</button>
                    </div>
                </div>
            `;
            
            document.body.appendChild(dialog);
            
            const okButton = dialog.querySelector('.btn-primary');
            okButton.addEventListener('click', () => {
                dialog.remove();
                resolve(true);
            });
            
            dialog.addEventListener('click', (e) => {
                if (e.target === dialog) {
                    dialog.remove();
                    resolve(true);
                }
            });
        });
    }
    
    static showConfirm(message, title = 'Confirm') {
        return new Promise((resolve) => {
            const dialog = document.createElement('div');
            dialog.className = 'custom-dialog-overlay';
            dialog.innerHTML = `
                <div class="custom-dialog">
                    <div class="custom-dialog-header">
                        <h3>${this.escapeHtml(title)}</h3>
                        <button class="custom-dialog-close" onclick="this.closest('.custom-dialog-overlay').remove(); window.__dialogResolve(false);">&times;</button>
                    </div>
                    <div class="custom-dialog-body">
                        <p>${this.escapeHtml(message)}</p>
                    </div>
                    <div class="custom-dialog-footer">
                        <button class="btn btn-secondary" onclick="this.closest('.custom-dialog-overlay').remove(); window.__dialogResolve(false);">Cancel</button>
                        <button class="btn btn-primary" onclick="this.closest('.custom-dialog-overlay').remove(); window.__dialogResolve(true);">Confirm</button>
                    </div>
                </div>
            `;
            
            document.body.appendChild(dialog);
            
            window.__dialogResolve = resolve;
            
            dialog.addEventListener('click', (e) => {
                if (e.target === dialog) {
                    dialog.remove();
                    resolve(false);
                }
            });
        });
    }
    
    static showPrompt(message, defaultValue = '', title = 'Input') {
        return new Promise((resolve) => {
            const dialog = document.createElement('div');
            dialog.className = 'custom-dialog-overlay';
            dialog.innerHTML = `
                <div class="custom-dialog">
                    <div class="custom-dialog-header">
                        <h3>${this.escapeHtml(title)}</h3>
                        <button class="custom-dialog-close" onclick="this.closest('.custom-dialog-overlay').remove(); window.__dialogResolve(null);">&times;</button>
                    </div>
                    <div class="custom-dialog-body">
                        <p>${this.escapeHtml(message)}</p>
                        <input type="text" class="custom-dialog-input" value="${this.escapeHtml(defaultValue)}" autofocus>
                    </div>
                    <div class="custom-dialog-footer">
                        <button class="btn btn-secondary" onclick="this.closest('.custom-dialog-overlay').remove(); window.__dialogResolve(null);">Cancel</button>
                        <button class="btn btn-primary" onclick="const input = this.closest('.custom-dialog').querySelector('.custom-dialog-input'); this.closest('.custom-dialog-overlay').remove(); window.__dialogResolve(input.value);">OK</button>
                    </div>
                </div>
            `;
            
            document.body.appendChild(dialog);
            
            const input = dialog.querySelector('.custom-dialog-input');
            input.focus();
            input.select();
            
            window.__dialogResolve = resolve;
            
            input.addEventListener('keypress', (e) => {
                if (e.key === 'Enter') {
                    dialog.remove();
                    resolve(input.value);
                } else if (e.key === 'Escape') {
                    dialog.remove();
                    resolve(null);
                }
            });
            
            dialog.addEventListener('click', (e) => {
                if (e.target === dialog) {
                    dialog.remove();
                    resolve(null);
                }
            });
        });
    }
    
    static escapeHtml(text) {
        if (!text) return '';
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }
}

// Replace browser default functions
window.alert = function(message) {
    return CustomDialog.showAlert(message, 'Alert');
};

window.confirm = function(message) {
    return CustomDialog.showConfirm(message, 'Confirm');
};

window.prompt = function(message, defaultValue) {
    return CustomDialog.showPrompt(message, defaultValue || '', 'Input');
};

