// JSON Formatter and Validator for BISEN
class JSONFormatter {
    static format(text) {
        if (!text || text.trim() === '') {
            return '';
        }
        
        try {
            const parsed = JSON.parse(text);
            return JSON.stringify(parsed, null, 2);
        } catch (e) {
            return text; // Return original if not valid JSON
        }
    }
    
    static validate(text) {
        if (!text || text.trim() === '') {
            return { valid: true, error: null };
        }
        
        try {
            JSON.parse(text);
            return { valid: true, error: null };
        } catch (e) {
            return { valid: false, error: e.message };
        }
    }
    
    static formatXML(text) {
        if (!text || text.trim() === '') {
            return '';
        }
        
        try {
            const parser = new DOMParser();
            const xmlDoc = parser.parseFromString(text, 'text/xml');
            
            if (xmlDoc.getElementsByTagName('parsererror').length > 0) {
                return text; // Return original if invalid
            }
            
            // Simple XML formatting
            let formatted = '';
            let indent = 0;
            const indentSize = 2;
            
            text.replace(/>\s*</g, '><').split(/>/).forEach((node, index) => {
                if (node.trim()) {
                    if (node.indexOf('</') === 0) {
                        indent--;
                    }
                    formatted += ' '.repeat(indent * indentSize) + node.trim() + '>\n';
                    if (node.indexOf('</') !== 0 && node.indexOf('/') !== 0) {
                        indent++;
                    }
                }
            });
            
            return formatted.trim();
        } catch (e) {
            return text;
        }
    }
}

