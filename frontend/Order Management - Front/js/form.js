//converting flat structure of FormData to expected JSON object
export function createFormObject(form) {
  let formObject = {};

  new FormData(form).forEach((value, key) => {
    const keys = key.split("[").map((k) => k.replace("]", ""));
    keys.reduce((acc, k, i) => {
      if (i === keys.length - 1) {
        if (Number.isInteger(parseInt(k))) {
          acc.push(value);
        } else {
          acc[k] = value;
        }
      } else {
        if (Number.isInteger(parseInt(keys[i + 1]))) {
          acc[k] = acc[k] || [];
        } else {
          acc[k] = acc[k] || {};
        }
      }
      return acc[k];
    }, formObject);
  });

  return formObject;
}
