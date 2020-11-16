import { throwError } from 'rxjs';

/**
 * Check if str is JSON
 * @param {string} rawString
 */
function isStringifiedJSON(rawString) {
    try {
        JSON.parse(rawString);
    } catch (e) {
        return false;
    }
    
    return true;
}
export function errorHandler(rawError) {
    let error = null;

    let defaultError = { 
        message: 'Internal Server Error'
    };

    if (rawError && rawError.message == "Timeout has occurred") {
        error =  {
            message: `Please contact your System Administrator.`
        }
        return throwError(error);
    }

    if (!rawError || (rawError && !rawError._body && !rawError.error)) {
        return throwError(defaultError);   
    }

    if (rawError && rawError._body) {
        error = isStringifiedJSON(rawError._body) ? rawError.json() : defaultError;
        if (rawError.status) {
            error.status = rawError.status;
        }
    }

    return throwError(error);
}

