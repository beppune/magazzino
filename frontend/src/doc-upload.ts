import { html, LitElement } from 'lit';
import '@vaadin/button';
//import '@vaadin/text-field';
//import '@axa-ch/input-text';

class DocUpload extends LitElement {

    render() {
        return html`
            <div>
                <vaadin-button id="helloButton">Click me!</vaadin-button>
                <input type="file" >
            </div>`;
    }
}

customElements.define('doc-upload', DocUpload);