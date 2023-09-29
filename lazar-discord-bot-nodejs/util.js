module.exports = {
    MAX_MESSAGE_LENGTH: 2000,
    limit: function (string, limit) {
        if (string.length <= limit) {
            return string; // Return the original string if it's within the limit.
        } else {
            return string.substring(0, limit); // Truncate the string if it exceeds the limit.
        }
    },
    sendLargeMessage: async function (interaction, message) {
		while (message.length > 0) {
			const chunk = message.substring(0, this.MAX_MESSAGE_LENGTH);
			message = message.substring(this.MAX_MESSAGE_LENGTH);
	
			await interaction.followUp(chunk);
		}
	},
};