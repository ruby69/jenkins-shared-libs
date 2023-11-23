import groovy.json.JsonSlurper

def authPortainer() {
    def response = httpRequest (consoleLogResponseBody: true,
        contentType: 'APPLICATION_JSON',
        httpMode: 'POST',
        requestBody: '{"username":"' + portainerUsername + '","password":"' + portainerPass + '"}',
        url: "$portainerUrl/api/auth",
        validResponseCodes: '200')
    def result = new JsonSlurper().parseText(response.content)
    result.jwt
}

def putStack(jwt, stackId, endpointId, stackContent) {
    httpRequest (consoleLogResponseBody: true,
        contentType: 'APPLICATION_JSON',
        httpMode: 'PUT',
        requestBody: "{\"stackFileContent\": \"$stackContent\",\"prune\": true,\"pullImage\": true}",
        url: "$portainerUrl/api/stacks/$stackId?endpointId=$endpointId",
        customHeaders:[[name:'Authorization', value:"Bearer $jwt"]],
        validResponseCodes: '200')
}

def getStack(jwt, stackId) {
    httpRequest (consoleLogResponseBody: true,
        contentType: 'APPLICATION_JSON',
        httpMode: 'GET',
        url: "$portainerUrl/api/stacks/$stackId",
        customHeaders:[[name:'Authorization', value:"Bearer $jwt"]],
        validResponseCodes: '200')
}

def getCommitMessage() {
    sh (script: 'git log -1 --pretty=%B $gitCommit', returnStdout: true).trim()
}